# demo 模块新增表存在判断测试

## 背景

`EzMapper` 已新增 `tableExists(DbTable table)` 接口，需要在 demo 模块当前支持的各数据库查询测试中补充真实调用用例，覆盖集成使用路径。

## 目标

在 MySQL、Oracle、PostgreSQL、SQL Server 和 DM 的查询测试类中新增表是否存在的测试用例，并尝试执行相关测试。

## 范围

本次仅修改 demo 模块各数据库的 `*ComplexEntitySelectTest`，新增 `ezTableExists` 测试方法，断言已有表存在、缺失表不存在。

## 非目标

不调整数据库连接配置，不新增数据库 schema，不修改已有查询测试逻辑。

## 关键设计

每个数据库查询测试通过 `this.sqlSession.getMapper(EzMapper.class)` 获取通用 Mapper，然后调用 `tableExists(DbTable.of("ez_complex_user"))` 和 `tableExists(DbTable.of("ez_complex_missing_table"))` 分别验证 true/false 路径。表名大小写交由各数据库基类中的 `NameCasePolicy` 处理。

## 验收标准

5 个数据库查询测试类都包含 `ezTableExists` 用例。

相关测试命令已执行，若数据库环境不可用，需要记录失败原因和风险。

## 任务清单

### 分析

- [x] 明确需求目标、范围和非目标
- [x] 确认影响文件、接口、配置或数据结构

### 实施

- [x] 完成核心修改或实现
- [x] 保持现有风格与最小必要改动

### 验证

- [x] 执行与改动直接相关的测试、构建或检查
- [x] 记录无法验证的原因与风险

### 收尾

- [x] 更新当前进度
- [x] 记录剩余风险与阻塞

## 当前进度

已在 5 个数据库查询测试中新增表存在判断用例。已执行新增用例定向验证，5 个数据库新增用例全部通过。已执行 5 个完整查询测试类，其中 MySQL、Oracle、PostgreSQL、DM 查询测试类通过，SQL Server 查询测试类存在既有 `OFFSET` 语法错误导致部分非本次新增用例失败。

## 风险与阻塞

demo 模块测试依赖真实数据库连接。当前环境可连接 5 类数据库；完整 SQL Server 查询测试类仍存在既有分页 SQL `OFFSET` 语法错误，失败用例不属于本次新增的 `ezTableExists` 用例。
