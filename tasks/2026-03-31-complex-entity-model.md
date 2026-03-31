# 完善测试实体模型与全库兼容支持

## 修改背景
当前的 `ez-mybatis-demo` 测试数据模型（如 `User`、`Org`、`UserOrg`）比较简单，大多只包含常见的 `String`、`Integer` 以及个别枚举和 `Date` 类型字段。这样相对简易的模型无法实现对框架底层多数据类型转换、JPA元数据提取策略的严格测试。特别是针对复杂的企业应用场景，需要更多维度去验证多表 JOIN、嵌套 JOIN，以及如加密存储（通过事件监听器修改状态）、JSON映射（TypeHandler扩展）等能力的稳定性。

## 需求
重构并建立一个更全面的且覆盖大多数业务场景的高阶测试全家桶，具体包括：
1. **全数据类型覆盖**：同时测试 `Long`, `Short`, `Byte`, `Double`, `BigDecimal`, `Boolean`, `Date`, Enum, 原生 Blob 大字段、Clob 长文本等字段的入库兼容与稳定性。
2. **定制行为验证**：
   - 提供能够专门用于自定义 TypeHandler 校验的映射字段（如：对象转换信息的 `extInfo`）。
   - 保留专用于测试数据源事件拦截器（`InsertListener`/`UpdateListener`）来进行入库与更新环节加解密的预留字段（如：`secretContent`），明确区分 TypeHandler 和全局钩子的职责。
3. **多表关联与嵌套结构测试**：构建完整的三级层级实体结构（`ComplexDepartment` <- `ComplexUser` <-> `ComplexOrder` <-> `ComplexOrderItem`），以支持多表关联与层级嵌套查询功能的验证。
4. **全库 DDL 支持**：生成相对应的包括 MySQL、PostgreSQL、Oracle、MSSQL、达梦（DM）在内的五种主流数据库的真实物理表结构 DDL 初始化脚本。
5. **Java Doc 注释规范化**：保证所有新增代码采用正规的 `/** */` JavaDoc 文档注释而非单行注释进行业务级标注。

## 任务列表
- [x] 新建全方位测试主实体类 (`ComplexUser`) 并完善各类基础及扩展类型的映射属性。
- [x] 创建 `ExtInfoJsonTypeHandler` 支撑自定义 JSON 与实体对象的自定义互相映射。
- [x] 修正 `secretContent` 的注解，明确基于 MyBatis 原生与自定义框架的底层全局事件机制改写加密测试的数据，移除非规范操作引出的 TypeHandler。
- [x] 新增用于测试多表 Join 关联及嵌套关联测试专用的支撑表映射实体类（`ComplexDepartment`, `ComplexOrder`, `ComplexOrderItem`）。
- [x] 修正并统一建立实体的所有属性和类的官方标准 Java Doc 注释。
- [x] 产出完全兼容对应架构的五种测试数据库（MySQL / PostgreSQL / Oracle / SqlServer / DM）的初始化 `schema-*.sql` 建表文件。
- [x] 设计完成通用的业务关联测试骨架 `ComplexEntityTest.java` ，并在其中提供了监听加解密用法示例代码片段。
- [x] 编写 `MySQL` 环境特有的复杂实体联查测试 `MySqlComplexEntityTest.java`。
- [x] 全量重构 `MySQL` 环境的 `MySqlComplexEntityInsertTest.java`，将基于 SaveTest 和 User 的 19 种核心插入与高级插入测试平移并对接完成。
- [x] 深度完善 Insert 的周边组件边界支持：在所有的 5 大流派 SQL 中扩展 `ez_complex_order_archive` 数据级归档表以解决 `insertByQuery` ID 穿透写入冲突。
- [x] 重构全部 `MySqlComplexEntityInsertTest` 内的方法命名，以 `ezBaseMapperXxx`、`ezMapperXxx` 与 `jdbcXxx` 精确对齐原封测接口意图（含长效性性能压测名）。
- [x] 所有模型的 ID 及时间管理收归体系底座（`AbstractBaseTest` 或全局拦截器）隐性操控，移除手动脏赋值代码。
- [x] 【已完成】全量重构 `MySQL` 环境的 `MySqlComplexEntityDeleteTest.java`
  - [x] 构建造桩 `ComplexUser` 工具法以闭环“先增后删”动作逻辑。
  - [x] 补齐 `ezBaseMapperDelete` 与 `ezMapperDelete` 基础系列的 16 种场景删除能力验证。
  - [x] 补齐基于 `EzDelete` 连表特性的多表联查精准删除测试及批量多重对象提交。
- [x] 【已完成】全量重构 `MySQL` 环境的 `MySqlComplexEntityUpdateTest.java`
  - [x] 基于 `ComplexUser` 搭建 `更新/覆盖式替换(Replace)` 单体测试、多表测试骨架。
  - [x] 改写基于旧设值的 `Formula` (算数)、`Function` (功能)、`CaseWhen` 等 7 种以上高阶 `EzUpdate` 特性测试并验证。
  - [x] 彻底转换 20+ 个方法全面抛弃过时 `.set().done()` 链与对底层数据老旧类的强相关依赖。
- [x] 【已完成】全量重构 `MySQL` 环境的 `MySqlComplexEntitySelectTest.java`
  - [x] 迁移并对齐 `MySqlSelectTest` 的全部 31 个测试用例，确保功能完全等价。
  - [x] 涵盖基础 BaseMapper、EzMapper 以及高级 EzQuery（含嵌套条件、Union、WindowFunction、子查询等）。
  - [x] 验证并发查询返回类型的一致性。
- [x] 【已完成】强化 `Where`、`Join`、`Having` 的 Lambda 构建能力
  - [x] 为 `groupCondition` 增加 `Consumer` 闭包重载，支持嵌套条件的自然表达。
- [x] 补齐 `MySqlComplexEntityUpdateTest` 中 EzMapper 层遗漏的 4 个 `ByTable` 变体方法（updateByTable, batchUpdateByTable, replaceByTable, batchReplaceByTable）。
- [x] 新建 `MySqlComplexEntityUpsertTest` 迁移 `MySqlUpsertTest` 的 2 个 Upsert 测试用例（upsertUpdateTest, upsertInsertTest）。
- [ ] 补齐其它数据库方言（Oracle, PostgreSQL, MsSQL, DM）的复杂实体测试用例。

## 完成进度
部分完成。MySQL 环境下的 Insert, Delete, Update, Select, Upsert 全部动作已完整迁移至 ComplexEntity 体系（含先前遗漏的 EzMapper ByTable 系列及 Upsert），并完成了核心构建器的 Lambda 强化。后续需将其余数据库方言的测试代码按此标准进行平移。
