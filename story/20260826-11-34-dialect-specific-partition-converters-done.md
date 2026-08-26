# 补充 PostgreSQL 和 SQL Server 专用分区转换器

## 背景

PostgreSQL 和 SQL Server 的方言提供者继承了 MySQL 的基础转换器注册。分区结构按具体类型查找转换器时，两个方言可能使用 MySQL 的 `PARTITION (...)` 表引用语法，生成不符合目标数据库语法的 SQL。

## 目标

为 PostgreSQL 和 SQL Server 提供并注册独立的普通分区、子分区转换器，避免错误复用 MySQL 分区语法。

## 范围

- 新增 PostgreSQL 普通分区和子分区转换器。
- 新增 SQL Server 普通分区和子分区转换器。
- 在对应方言提供者中按 `NormalPartition.class` 和 `SubPartition.class` 注册。
- 对无法通过表引用表达的非空分区请求快速失败，并给出路由提示。
- 补充转换器注册和行为测试。

## 非目标

- 不为 PostgreSQL 自动推导分区子表名称。
- 不为 SQL Server 自动生成分区键条件或 `$PARTITION` 表达式。
- 不调整 PostgreSQL、SQL Server 现有 schema/tableName 动态路由逻辑。

## 关键设计

PostgreSQL 和 SQL Server 的表引用均不使用 MySQL/Oracle 风格的 `PARTITION (...)` 后缀，因此专用转换器对空分区保持无输出，对非空分区抛出 `UnsupportedOperationException`。调用方应将 PostgreSQL 目标分区作为动态物理 `tableName`，SQL Server 则通过分区键条件或数据库原生分区能力定位分区。

由于 `SubPartition` 是 `NormalPartition` 的子类，两个具体类型分别注册独立转换器，确保基于运行时结构类型的精确查找不会回退到错误的 MySQL 转换器。

## 验收标准

- [x] PostgreSQL 注册专用普通分区和子分区转换器。
- [x] SQL Server 注册专用普通分区和子分区转换器。
- [x] PostgreSQL 和 SQL Server 的非空分区表引用不会生成 MySQL `PARTITION (...)` 语法。
- [x] 现有各方言动态 schema、tableName 和已支持分区 SQL 不受影响。
- [x] 相关单元测试和项目构建通过。

## 任务清单

### 分析

- [x] 确认 PostgreSQL、SQL Server 当前继承 MySQL 分区转换器的注册路径。
- [x] 确认 `NormalPartition`、`SubPartition` 需要按具体类型分别注册。

### 实施

- [x] 新增 PostgreSQL 专用分区转换器。
- [x] 新增 SQL Server 专用分区转换器。
- [x] 完成两个方言提供者的转换器注册。

### 验证

- [x] 验证两个方言的转换器注册类型。
- [x] 验证非空分区请求快速失败且不生成错误后缀。
- [x] 执行相关测试和项目构建。

### 收尾

- [x] 更新当前进度。
- [x] 记录剩余风险与阻塞。

## 当前进度

已完成专用转换器实现、方言注册及测试验证。需求已完成。

## 风险与阻塞

PostgreSQL 和 SQL Server 的分区定位需要结合实际表结构或分区键条件，当前实现只负责阻止错误的表引用语法，不自动完成数据库特有的分区定位表达式。
