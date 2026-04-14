package org.rdlinux.ezmybatis.core.sqlgenerate;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.sqlstruct.EntityField;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

/**
 * SQL 生成阶段的上下文对象。
 * <p>
 * 该类用于在 SQL 结构树遍历、方言转换、参数占位符生成等过程中，集中保存一次 SQL 生成所需的共享状态，
 * 以避免各个 SQL 节点在递归解析时反复透传大量参数。一个 {@code SqlGenerateContext} 实例通常对应一次完整的
 * SQL 片段或 SQL 语句生成过程。
 * </p>
 *
 * <p>
 * 当前上下文主要承担以下职责：
 * </p>
 * <ul>
 *     <li>持有 MyBatis 的 {@link Configuration}，供底层参数处理和配置读取使用。</li>
 *     <li>持有 {@link MybatisParamHolder}，统一访问 Mapper 原始参数。</li>
 *     <li>持有当前 SQL 输出缓冲区 {@link StringBuilder}，用于持续拼接生成结果。</li>
 *     <li>通过 {@code accessFieldStack} 记录当前正在访问的实体字段链路，使右值解析阶段也能感知左值字段语义。</li>
 * </ul>
 *
 * <p>
 * 其中 {@code accessFieldStack} 的设计主要用于解决“左右操作数分开解析，但右侧又需要知道左侧字段信息”的问题。
 * 例如在解析条件表达式时，左侧可能先解析出某个 {@link EntityField}，随后右侧参数对象在生成占位符、
 * 执行参数转换、或接入字段级扩展能力（如加解密、敏感字段处理）时，需要知道当前对应的是哪个实体字段，
 * 此时就可以通过上下文栈顶字段进行关联。
 * </p>
 *
 * <p>
 * 该对象支持从 MyBatis 参数 Map 或已有的 {@link MybatisParamHolder} 快速构建，也支持通过
 * {@link #copyOf(SqlGenerateContext)} 和 {@link #copyOf(SqlGenerateContext, StringBuilder)} 复制已有上下文。
 * 复制时会复用原有配置、参数持有器以及字段访问栈，但可以切换新的 SQL 构建器，适合在子表达式、
 * 嵌套 SQL 片段或临时 SQL 生成场景下复用同一批上下文信息。
 * </p>
 *
 * <p>
 * 需要注意：该类本质上是一次生成过程内的可变上下文，不是线程安全对象，预期使用方式是在单次 SQL 生成流程中短生命周期使用。
 * </p>
 */
@Getter
public class SqlGenerateContext {
    private final Configuration configuration;
    private final MybatisParamHolder mybatisParamHolder;
    /**
     * 当前访问的字段栈。
     * 用于在解析左侧操作数（如 EntityField）时将信息传导至右侧（如 ObjArg 参数值），以支持诸如参数加解密的扩展插件获取当前对应的模型字段。
     */
    private Deque<EntityField> accessFieldStack = new LinkedList<>();
    @Setter
    private StringBuilder sqlBuilder;

    private SqlGenerateContext(Configuration configuration, MybatisParamHolder mybatisParamHolder) {
        this(new StringBuilder(), configuration, mybatisParamHolder);
    }

    private SqlGenerateContext(StringBuilder sqlBuilder, Configuration configuration,
                               MybatisParamHolder mybatisParamHolder) {
        this.sqlBuilder = sqlBuilder;
        this.configuration = configuration;
        this.mybatisParamHolder = mybatisParamHolder;
    }

    private SqlGenerateContext(StringBuilder sqlBuilder, Deque<EntityField> accessFieldStack,
                               Configuration configuration,
                               MybatisParamHolder mybatisParamHolder) {
        this.sqlBuilder = sqlBuilder;
        this.accessFieldStack = accessFieldStack;
        this.configuration = configuration;
        this.mybatisParamHolder = mybatisParamHolder;
    }

    /**
     * 基于 MyBatis 原始参数 Map 创建 SQL 生成上下文。
     * <p>
     * 该方法通常用于 SQL 生成流程的入口阶段。当调用方拿到的是 Mapper 方法执行时传入的原始参数 Map 时，
     * 可以通过该方法提取其中预先存放的 {@link Configuration}，并进一步包装为 {@link MybatisParamHolder}，
     * 最终构造出一个可直接参与 SQL 结构遍历和参数解析的上下文对象。
     * </p>
     *
     * @param param MyBatis 原始参数 Map，内部需包含
     *              {@link EzMybatisConstant#MAPPER_PARAM_CONFIGURATION} 对应的配置对象
     * @return 新创建的 SQL 生成上下文，默认持有新的 {@link StringBuilder}
     */
    public static SqlGenerateContext fromMyBatisParam(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        MybatisParamHolder paramHolder = new MybatisParamHolder(configuration, param);
        return new SqlGenerateContext(configuration, paramHolder);
    }

    /**
     * 基于已有的 {@link MybatisParamHolder} 创建 SQL 生成上下文。
     * <p>
     * 适用于调用方已经完成参数包装，仅希望补齐 SQL 构建器与上下文状态的场景。
     * 方法内部会从参数持有器中读取 MyBatis {@link Configuration}，并创建新的 SQL 输出缓冲区。
     * </p>
     *
     * @param paramHolder 已封装好的 MyBatis 参数持有器
     * @return 新创建的 SQL 生成上下文，默认持有新的 {@link StringBuilder}
     */
    public static SqlGenerateContext from(MybatisParamHolder paramHolder) {
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        return new SqlGenerateContext(configuration, paramHolder);
    }

    /**
     * 基于已有的 {@link MybatisParamHolder} 和指定的 SQL 构建器创建上下文。
     * <p>
     * 适用于调用方希望复用外部传入的 {@link StringBuilder} 来承接 SQL 输出的场景，
     * 例如在已有 SQL 片段基础上继续拼接、或将多个子结构输出到同一个缓冲区中。
     * </p>
     *
     * @param paramHolder 已封装好的 MyBatis 参数持有器
     * @param sqlBuilder  用于接收 SQL 输出的构建器
     * @return 新创建的 SQL 生成上下文
     */
    public static SqlGenerateContext from(MybatisParamHolder paramHolder, StringBuilder sqlBuilder) {
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        return new SqlGenerateContext(sqlBuilder, configuration, paramHolder);
    }

    /**
     * 复制已有上下文，并替换为新的 SQL 构建器。
     * <p>
     * 新上下文会复用原上下文中的配置、参数持有器以及字段访问栈，以保证子流程仍能感知当前参数环境和字段访问状态；
     * 同时切换到新的 {@link StringBuilder}，从而将输出结果隔离到新的缓冲区中。
     * </p>
     *
     * @param context    原始上下文
     * @param sqlBuilder 新的 SQL 构建器
     * @return 复制得到的新上下文
     */
    public static SqlGenerateContext copyOf(SqlGenerateContext context, StringBuilder sqlBuilder) {
        return new SqlGenerateContext(sqlBuilder, context.getAccessFieldStack(), context.getConfiguration(),
                context.getMybatisParamHolder());
    }

    /**
     * 复制已有上下文，并为其分配新的空 SQL 构建器。
     * <p>
     * 当调用方希望在保留原有配置、参数环境和字段访问状态的前提下，独立生成一段新的 SQL 片段时，
     * 可以直接使用该方法创建副本，而无需手动重新组装上下文信息。
     * </p>
     *
     * @param context 原始上下文
     * @return 复制得到的新上下文，内部持有新的空 {@link StringBuilder}
     */
    public static SqlGenerateContext copyOf(SqlGenerateContext context) {
        return new SqlGenerateContext(new StringBuilder(), context.getAccessFieldStack(), context.getConfiguration(),
                context.getMybatisParamHolder());
    }

    /**
     * 将当前正在处理的实体字段压入访问栈。
     * <p>
     * 该方法通常在解析左值字段时调用，用于将当前字段语义传递给后续的右值解析流程。
     * 若传入值为 {@code null}，则不会执行入栈操作。
     * </p>
     *
     * @param entityField 当前正在处理的实体字段
     */
    public void pushAccessField(EntityField entityField) {
        if (entityField != null) {
            this.accessFieldStack.push(entityField);
        }
    }

    /**
     * 将当前字段从访问栈中弹出。
     * <p>
     * 该方法通常与 {@link #pushAccessField(EntityField)} 成对使用，用于在字段相关的解析逻辑结束后恢复上层访问上下文。
     * 如果当前访问栈为空，则不会执行任何操作。
     * </p>
     */
    public void popAccessField() {
        if (!this.accessFieldStack.isEmpty()) {
            this.accessFieldStack.poll();
        }
    }

    /**
     * 获取当前访问栈栈顶的实体字段。
     * <p>
     * 返回值通常供右值参数处理、占位符生成或字段级扩展逻辑读取，以判断当前绑定的是哪个模型字段。
     * 如果当前没有正在访问的字段，则返回 {@code null}。
     * </p>
     *
     * @return 当前上下文中最近一次压栈且尚未弹出的实体字段；若不存在则返回 {@code null}
     */
    public EntityField getCurrentAccessField() {
        return this.accessFieldStack.peek();
    }

    /**
     * 获取 MyBatis 原始参数 Map 中的指定参数值。
     * <p>
     * 该方法直接委托给 {@link MybatisParamHolder}，仅用于读取 Mapper 原始入参中保存的值，
     * 不负责解析额外的派生语义或 SQL 生成过程中的临时状态。
     * </p>
     *
     * @param paramName 参数名称
     * @param <T>       参数值类型
     * @return 对应名称的参数值；若不存在则返回 {@code null}
     */
    public <T> T getParam(String paramName) {
        return this.mybatisParamHolder.get(paramName);
    }
}
