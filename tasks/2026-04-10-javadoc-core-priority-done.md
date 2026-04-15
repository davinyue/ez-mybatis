# 核心类 JavaDoc 补充

## 修改背景
- 当前项目不少公开核心类缺少足够的类级 JavaDoc，或仅有标题式注释，导致首次阅读时难以快速理解职责边界、使用入口和约束条件。
- 其中 `EzMybatisContent`、实体元数据工厂、`Select` 结构节点以及 `Merge` / `MySqlUpsert` 扩展 DSL 都属于高频入口或高复杂实现，优先补齐收益最高。

## 需求
- 为本轮选定的核心类补充更完整的 JavaDoc。
- 覆盖类、属性、构造方法、静态方法、普通方法以及参数说明。
- 保持最小必要修改，不改变原有逻辑与行为。

## 任务列表
- [x] 为 `EzMybatisContent` 补充类级说明，并完善核心属性、初始化、方言、监听器和销毁相关方法注释。
- [x] 为 `EzEntityClassInfoFactory` 补充类级说明，并完善缓存、热加载通知与清理方法注释。
- [x] 为 `Select` 及其构造器补充更明确的语义说明。
- [x] 为 `Merge` 及其 Builder 补充扩展 DSL 的用途、约束和参数说明。
- [x] 为 `MySqlUpsert` 及其 Builder 补充用途、约束和参数说明。
- [x] 为 `EzResultSetHandler` 及其接入拦截器补充结果映射主链路说明。
- [x] 为 `EzMybatisUpdateInterceptor` 补充监听触发入口及参数说明。
- [x] 为 `EzMapper` 与 `EzBaseMapper` 补充接口级说明与方法名常量说明。

## 完成进度
- 当前状态：已完成。
- 已完成：
  - 已新增 `EzMybatisContent` 类级 JavaDoc，明确其作为运行时上下文中心的职责、初始化内容与销毁范围。
  - 已补充 `EzEntityClassInfoFactory` 的缓存构建、热加载通知和清理入口说明。
  - 已补充 `Select` 的结构节点语义、构建入口和 Builder 关键方法注释。
  - 已补充 `Merge` 的跨数据库扩展能力说明、构造约束和校验方法注释。
  - 已补充 `MySqlUpsert` 的 SQL 语义、实体约束和 Builder 方法注释。
  - 已补充 `EzResultSetHandler` 的类级说明、字段说明，以及结果集处理、自动映射、嵌套映射、构造器映射和类型处理器选择主链路上的方法与参数说明。
  - 已补充 `EzMybatisResultSetHandlerInterceptor` 与 `ResultSetLogic` 的接入说明，明确默认结果处理器被替换为 Ez-MyBatis 处理器的条件与流程。
  - 已补充 `EzMybatisUpdateInterceptor` 的类级说明、监听器列表字段说明，以及插入、更新、删除、DSL 更新删除监听触发方法和主拦截入口的参数说明。
  - 已补充 `EzMapper` 与 `EzBaseMapper` 的接口级 JavaDoc，明确动态 Mapper 与强类型 Mapper 的职责区别，并为 `EzMapper` 中对外暴露的方法名常量增加说明。
- 未完成：
  - `EzResultSetHandler` 仍有少量内部实现分支未逐个补齐注释；如需做到“私有方法 100% 全覆盖”，建议继续单独推进第三批。
