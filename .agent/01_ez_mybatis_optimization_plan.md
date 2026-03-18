# EZ-Mybatis 项目重构与优化实施计划 (文档编号: EZ-OPT-001)

本计划旨在通过四个阶段逐步重构 ez-mybatis 项目，以解决其现存的架构与设计问题，提升代码的可维护性、安全性和扩展能力。

## 阶段 1：消除安全隐患与内存泄漏风险（高优先级）

**目标**：解决可能导致生产事故的底层隐患，提升框架在复杂生产环境下的健壮性。

- ~~**1.1 改造 `EzMybatisContent` 中的全局状态管理**~~ （**不需要改造**）
  - 经分析，`Configuration` 是随 `SqlSessionFactory` 创建的单例 Bean，生命周期与 Spring `ApplicationContext` 一致，不存在实际的内存泄漏风险。`WeakHashMap` 替换反而可能导致配置被意外 GC，保持现状。
- [x] **1.2 安全处理与重构 `ThreadLocal` 上下文 (`CURRENT_ACCESS_FIELD`)**
  - **问题分析**：因字段加密等扩展点需求，在解析 `EntityField` 和 `ObjArg` 时存在关联逻辑。当前依靠全局 `ThreadLocal` 传递关联状态且无 `try-finally` 保护，存在内存泄露与脏数据的严重生产隐患。
  - **重构方案**：废弃 `ThreadLocal` 设计。引入 `SqlGenerateContext` 上下文对象替代松散的多个参数传递，并在该上下文内部维护状态栈，使得遍历状态的生命周期严格随方法栈同生共死，从根本上兼顾扩展性与安全性。
  - **执行进度**：
    - [x] 1. 创建 `SqlGenerateContext` 类，将原散件参数（`StringBuilder`, `Configuration`, `MybatisParamHolder`）及 `ThreadLocal` 的访问标识栈放入其中。
    - [x] 2. 改造基础接口 `Converter` 的签名 `buildSql` 以及对应的抽象基类 `AbstractConverter`，使其统一接收 `SqlGenerateContext`。
    - [x] 3. 批量扫描项目(通过脚本辅助加速)，完成各具体 Converter 及底层生成器 (`AbstractEzQueryToSql` 等) 的老签名替换，统一使用新上下文对象，并在涉及到访问标识入栈、出栈的逻辑中移除 `EzMybatisContent.CURRENT_ACCESS_FIELD` 强依赖。
    - [x] 4. 处理并消除遗留的编译报错信息，确认相关单测顺利通过。

## 阶段 2：重构多数据库方言支持，符合“开闭原则”（中优先级）

**目标**：消除核心设计硬编码关联，使得新增数据库支持能以插拔方式实现，无需修改引擎源码。

- [x] **2.1 重构 `SqlGenerateFactory` 与 `EzMybatisContent#initDbType`**
  - 引入 SPI（服务提供者接口）机制。提取并规范化方言生成器接口。
  - 将现有的 `MySqlSqlGenerate`、`OracleSqlGenerate`、`DmSqlGenerate` 等组件重构为符合 SPI 规范的服务提供者并完成服务注册(`META-INF/services/`)。
  - 将基于 `switch` / `if-else` 的静态类加载器模式改造为 `ServiceLoader` 的动态方言发现和匹配逻辑。

## 阶段 3：治理“上帝接口与上帝类”（中优先级）

**目标**：隔离业务复杂度，遵循单一职责和接口隔离原则。

- ~~**3.1 拆分 `EzMapper` 接口隔离**~~ （**不需要改造**）
  - 经分析，拆分接口会破坏现有公共 API 兼容性，且用户已习惯通过单一 `EzMapper` 进行操作。改造收益有限，保持现状。
- ~~**3.2 瘦身并分层 `EzDao`**~~ （**不需要改造**）
  - 经分析，DAO 中的方法虽较多但各自职责明确，均为简单的参数校验+委托调用模式。引入 Repository 模式改造成本大且会增加代码复杂度，保持现状。

## 阶段 4：收敛 JDBC 底层实现与类型安全增强（长期计划）

**目标**：统一底层操作风格，保证在拦截器链和事务体系下的全生命周期托管与类型安全。

- ~~**4.1 改造 `JdbcInsertDao` 和 `JdbcUpdateDao`**~~ （**不需要改造**）
  - 经分析，虽然内部直接操作 JDBC `PreparedStatement`，但 `Connection` 来自 Spring 管理的 `SqlSessionTemplate`，事务仍由 Spring 统一管理，不存在事务脱管风险。当前原生 JDBC 批量方式（多 VALUES 拼接）性能优于 `ExecutorType.BATCH`，保持现状。
- ~~**4.2 优化隐式类型（字符串到常量的重构）**~~ （**不需要改造**）
  - 经分析，方法名和参数名已通过 `public static final String` 常量和 `EzMybatisConstant` 统一管理，不存在魔法字符串。`@XxxProvider.method` 的字符串约束是 MyBatis 框架的固有限制，无法绕过。LanguageDriver/jOOQ 的改造成本远大于收益，保持现状。
