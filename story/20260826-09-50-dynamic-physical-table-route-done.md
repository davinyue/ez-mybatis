# 动态物理表路由支持

## 背景

当前 Ez-MyBatis 已通过 `DbTable`、`EntityTable`、`SqlTable` 和 `EzQueryTable` 描述不同类型的表结构。现有表名解析主要返回固定表名，无法在 SQL 生成时根据租户、数据源或业务路由动态决定 schema、table name 和 partition。

其中 `EntityTable` 的基础表名可能来自显式参数、实体 `@Table` 注解或实体类名转换；`SqlTable` 和 `EzQueryTable` 的 `getTableName()` 返回的是派生表别名，不能统一按物理表名处理。

同时，`ez-mybatis-spring2-project` 和 `ez-mybatis-spring3-project` 需要支持通过 Spring Bean 注入动态物理表路由逻辑。

## 目标

在不修改实体元数据缓存、不修改派生表语义、不为每个物理表生成 Mapper Bean 的前提下，支持在 SQL 生成阶段动态解析：

- schema；
- table name；
- partition。

同时支持 Spring2、Spring3 及其 Boot Starter 注入动态路由 Bean，并保证路由结果按单次 SQL 生成隔离、可并发使用。

## 范围

本次支持 Ez-MyBatis 自身生成的物理表 SQL，包括：

- `EntityTable` 和 `DbTable` 的查询、插入、更新、删除；
- EzQuery、EzUpdate、EzDelete 中的物理表和 Join 表；
- 相关数据库方言转换器；
- MySQL Upsert、Merge 等扩展中使用的物理表；
- Spring2、Spring3 的动态路由 Bean 注入。

动态路由只作用于 `DbTable` 及其子类 `EntityTable`。`SqlTable`、`EzQueryTable` 作为派生表，仅保留原有别名和子查询渲染逻辑；嵌套查询中的 `EntityTable` 仍按物理表规则解析。

## 非目标

- 不修改 `EntityClassInfo` 中缓存的实体字段、默认表名和 schema 元数据；
- 不通过修改或回写原始 `DbTable` 保存请求级路由结果；
- 不把 `model` 实例放入动态路由上下文；
- 不自动改写 XML Mapper 和 `selectBySql`、`updateBySql` 等原生 SQL；
- 不为租户、月份或分片表动态注册独立的 Mapper、Service 或 Spring Bean；
- 不在本次需求中实现按单条记录自动拆分批量 SQL。

## 关键设计

### 1. 动态表名升级为动态物理表路由

每个 MyBatis `Configuration` 只注册一个动态路由器。Resolver 不再只返回字符串，而是返回包含 schema、table name 和 partition 的物理路由结果：

```text
DynamicTableResolver
    -> PhysicalTableRoute(schema, tableName, partition)
```

Resolver 不通过多个 Bean 的 `supports()` 和 `order()` 隐式竞争。多个动态路由 Bean 同时存在时，Spring 初始化阶段直接抛出配置异常。需要组合多个业务规则时，由使用方显式实现一个组合 Resolver，并在组合逻辑中保证最多一个规则匹配。

`PhysicalTableRoute` 中的 partition 使用现有 `Partition` 结构对象，不直接返回 `PARTITION(...)` SQL 字符串，由数据库方言转换器负责最终渲染。

### 2. 路由上下文不保存重复的表结构属性

路由上下文至少包含：

- MyBatis `Configuration`；
- 当前 `DbTable`；
- `baseTableName`。

`baseTableName` 在进入 Resolver 前计算，来源为当前表实现的基础 `getTableName(configuration)`：

- 显式 `EntityTable` 使用显式表名；
- 非显式 `EntityTable` 使用实体注解或默认命名得到的表名；
- `DbTable` 使用自身表名。

schema 和 partition 不在上下文中复制，需要基础值时直接从 `DbTable` 获取。

上下文不包含 model。动态路由以语句级请求上下文、租户上下文或其他外部路由服务为依据；依赖单条 model 字段决定表名的场景由调用方先解析物理表后显式传入。

### 3. 基础值与动态值合并规则

- Resolver 返回 `null`：完整使用当前 `DbTable` 的 schema、基础 table name 和 partition；
- Resolver 返回非空 `PhysicalTableRoute`：完整使用其中的 schema、tableName 和 partition，不再对单个字段做基础值回退；
- 非空 `PhysicalTableRoute` 的 tableName 必须非空；schema 和 partition 可以为 `null`，表示明确不使用 schema 或 partition；
- 因此显式 `EntityTable.of(User.class, "user_custom")` 的表名也允许被动态替换。

未配置 Resolver 或 Resolver 返回 `null` 时，现有表结构行为保持不变。

### 4. 解析结果不回写原始 DbTable

每次 SQL 生成时创建当前语句使用的物理路由结果，方言转换器使用解析结果渲染 schema、table name 和 partition。禁止通过 `setSchema`、`setTableName` 或 `setPartition` 修改原始表对象，避免对象复用和并发请求之间串表。

### 5. 方言转换边界

动态解析发生在物理表转换层：

- 物理表转换器读取解析后的 schema、table name 和 partition；
- partition 继续委托已有方言 `Partition` Converter；
- 动态 schema、table name 和 partition 只作为标识符参与渲染，必须执行标识符校验和方言转义；
- 不允许 Resolver 返回任意 SQL 片段。

需要检查并统一处理当前绕过物理表转换器、直接调用 `getTableName(configuration)` 的 SQL 生成和扩展代码。

### 6. Spring 注入

在 Spring2 和 Spring3 各自的 `SpringEzMybatisInit` 中获取 `DynamicTableResolver` 类型 Bean：

- 没有 Bean：不启用动态路由；
- 只有一个 Bean：注册到当前 MyBatis `Configuration` 对应的 Ez-MyBatis 上下文；
- 存在多个 Bean：初始化阶段直接失败，并提示 Bean 名称。

如需组合多个规则，由用户提供单个组合 Resolver；组合 Resolver 内部如果发现多个规则同时匹配，必须直接抛出异常，不能依赖顺序静默选择。

Spring Boot Starter 继续复用已有的 `SpringEzMybatisInit.init(...)` 调用链，不额外注册按物理表拆分的 Mapper Bean。核心模块不依赖 Spring。

### 7. 批量与并发约束

- 一次生成的批量 SQL 使用同一组物理路由；
- 不支持根据批量集合中每个 model 隐式切换物理表；
- 不同路由键的数据由调用方先分组后分别执行；
- 路由解析结果必须是单次 SQL 生成内的局部对象，支持不同线程使用不同租户上下文。

## 验收标准

- `EntityTable` 未显式指定表名时，可以动态生成 schema、table name 和 partition；
- `EntityTable.of(Entity.class, "custom_table")` 的基础表名可以被动态 Resolver 替换；
- `DbTable` 未指定 schema 或 partition 时，动态 Resolver 可以补充对应值；
- Resolver 返回非空结果时，schema、tableName、partition 均以路由结果为准；
- Resolver 返回 `null` 时，完整保留原表的 schema、tableName、partition；
- 非空 `PhysicalTableRoute` 的 tableName 不能为空；
- `SqlTable`、`EzQueryTable` 不触发物理表动态解析；
- 查询、插入、更新、删除、DSL Join、Upsert、Merge 等物理表 SQL 使用统一路由结果；
- 未注册 Resolver 时，现有 SQL 输出保持不变；
- Resolver 上下文不包含 model；
- 每个 MyBatis `Configuration` 最多注册一个动态路由器；
- 同时存在多个 Spring 动态路由 Bean 时能够快速失败并给出明确错误；
- 原始 `DbTable` 不被动态解析结果修改；
- Spring2 可以注入并使用动态路由 Bean；
- Spring3 可以注入并使用动态路由 Bean；
- 不同并发请求使用不同路由时不会发生表名串用；
- 动态 schema、table name、partition 均不能注入任意 SQL 片段。

## 任务清单

### 分析

- [x] 明确动态路由覆盖 schema、table name 和 partition
- [x] 确认 `DbTable`、`EntityTable`、`SqlTable`、`EzQueryTable` 的处理边界
- [x] 确认 `baseTableName` 需要在进入 Resolver 前由当前 Table 实现解析
- [x] 确认上下文不包含 model，路由结果不能回写原始 `DbTable`
- [x] 确认 Spring2、Spring3 的 Bean 注入入口

### 实施

- [x] 新增动态物理表路由接口、上下文和解析结果对象
- [x] 在 Ez-MyBatis 内容配置中按 MyBatis `Configuration` 注册单个 Resolver
- [x] 改造物理表转换器，使其统一使用解析后的 schema、table name 和 partition
- [x] 修复未经过物理表转换器的标准 CRUD 和扩展 SQL 路径
- [x] 在 Spring2、Spring3 初始化流程中注入单个动态路由 Bean，并对多个 Bean 快速失败
- [x] 增加标识符校验、空路由回退和非空路由整体替换约束

### 验证

- [x] 补充核心模块物理表路由单元测试
- [x] 补充各数据库方言的 schema、table name、partition SQL 测试
- [x] 补充 EntityTable、DbTable、SqlTable、EzQueryTable 边界测试
- [x] 补充 Spring2、Spring3 Bean 注入测试
- [x] 验证解析结果不回写原始 DbTable
- [x] 执行与改动直接相关的 Maven 测试和构建验证

### 收尾

- [x] 更新当前进度
- [x] 记录剩余风险与阻塞

## 当前进度

已完成 core 动态物理表路由实现、标准 SQL 和扩展 SQL 路径接入，以及 Spring2/Spring3 单 Resolver Bean 注入。路由结果合并逻辑使用 `if/else`：Resolver 返回 `null` 时完整沿用基础表，返回非空 `PhysicalTableRoute` 时完整采用其三个字段，并要求 tableName 非空。

已新增各方言 SQL 生成层测试：MySQL、PostgreSQL、SQL Server、Oracle、达梦均验证动态 schema 和 tableName，MySQL、Oracle、达梦同时验证动态 partition；并新增 Spring2、Spring3 基于 `ApplicationContext` 的单 Bean 注入和多 Bean 快速失败测试。MySQL 普通分区转换器同时补齐了 `PARTITION (` 前缀。

验证结果：根项目使用 JDK8 执行 `mvn -q clean test` 通过；Spring2 使用 JDK8 执行 `mvn -q clean test` 通过；Spring3 使用 JDK17 执行 `mvn -q clean test` 通过。

## 风险与阻塞

- 方言验证为 SQL 生成层单元测试，未连接真实数据库；真实数据库集成仍依赖外部数据库环境，未纳入本次自动化测试；
- PostgreSQL 和 SQL Server 本次仅验证 schema/tableName，未定义独立的表 partition SQL 语法；
- XML Mapper 和原生 SQL 暂不纳入本次自动改写范围。
