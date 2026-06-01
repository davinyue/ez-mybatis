# EzMapper 新增表存在判断接口

## 背景

当前通用 `EzMapper` 缺少直接判断物理表是否存在的接口，使用方需要自行编写不同数据库的系统目录查询 SQL，无法复用 Ez-MyBatis 已有方言能力。

## 目标

为 `EzMapper` 新增基于 `DbTable` 入参的表存在判断接口，并兼容当前支持的 MySQL、Oracle、PostgreSQL、SQL Server 和 DM。

## 范围

本次新增 `EzMapper#tableExists(DbTable table)`，由 `EzSelectProvider` 委托当前数据库方言生成系统目录查询 SQL，并补充核心模块单元测试覆盖各数据库 SQL。

## 非目标

不新增建表、删表或 DDL 管理能力；不调整 `DbTable` 结构；不引入新的 Mapper 类型；不执行真实数据库集成测试。

## 关键设计

Mapper 层新增 `boolean tableExists(DbTable table)`，保持入参与表结构对象一致。Provider 层从 MyBatis 参数中读取 `DbTable` 与 `Configuration`，委托 `SqlGenerate#getTableExistsSql`。方言层分别查询当前数据库的系统目录：MySQL/PostgreSQL 使用 `information_schema.tables`，SQL Server 使用 `INFORMATION_SCHEMA.TABLES`，Oracle/DM 使用 `ALL_TABLES`。未指定 schema 时使用当前 schema 函数。

## 验收标准

`EzMapper` 暴露 `tableExists(DbTable table)` 接口。

MySQL、Oracle、PostgreSQL、SQL Server、DM 都能生成对应的表存在查询 SQL。

指定 schema 和未指定 schema 两种场景都有测试覆盖。

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

已新增测试并完成核心实现。根据反馈，已将表名参数与 schema 条件拼接逻辑从 `AbstractSelectSqlGenerate` 下沉到各方言实现，避免抽象类承载过轻的表存在判断细节。已执行 `mvn -pl ez-mybatis-core clean test`，核心模块 47 个测试通过。

## 风险与阻塞

暂无已知阻塞。当前验证以 SQL 生成单元测试和核心模块构建为主，未连接真实数据库执行系统目录查询。
