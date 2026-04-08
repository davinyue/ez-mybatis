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
 * SQL生成上下文，用于在AST树（SQL结构树）构建过程中传递遍历状态。
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
     * 从mybatis param对象构建content
     */
    public static SqlGenerateContext ofMyBatisParam(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        MybatisParamHolder paramHolder = new MybatisParamHolder(configuration, param);
        return new SqlGenerateContext(configuration, paramHolder);
    }

    /**
     * 从一个存在的content拷贝content, 并且设置新的sql builder对象
     */
    public static SqlGenerateContext copyOf(SqlGenerateContext context, StringBuilder sqlBuilder) {
        return new SqlGenerateContext(sqlBuilder, context.getAccessFieldStack(), context.getConfiguration(),
                context.getMybatisParamHolder());
    }

    /**
     * 从一个存在的content拷贝content, 并且设置空的sql builder对象
     */
    public static SqlGenerateContext copyOf(SqlGenerateContext context) {
        return new SqlGenerateContext(new StringBuilder(), context.getAccessFieldStack(), context.getConfiguration(),
                context.getMybatisParamHolder());
    }

    /**
     * 将当前操作的字段压入上下文栈
     */
    public void pushAccessField(EntityField entityField) {
        if (entityField != null) {
            this.accessFieldStack.push(entityField);
        }
    }

    /**
     * 将当前操作的字段弹出上下文栈
     */
    public void popAccessField() {
        if (!this.accessFieldStack.isEmpty()) {
            this.accessFieldStack.poll();
        }
    }

    /**
     * 获取上下文栈顶正在操作的字段，供右侧扩展解析使用
     */
    public EntityField getCurrentAccessField() {
        return this.accessFieldStack.peek();
    }

    /**
     * 该方法只能获取mybatis原始map里面的参数值
     *
     * @param paramName 参数名称
     * @return 参数值
     */
    public <T> T getParam(String paramName) {
        return this.mybatisParamHolder.get(paramName);
    }
}
