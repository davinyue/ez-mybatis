# 修复 Oracle ROWNUM 分页 SQL 拼接错误

## 背景

Oracle 未启用 `enableOracleOffsetFetchPage` 时，查询第二页及后续页会使用 ROWNUM 分页。无排序、无分组查询在组装外层分页 SQL 时，原 SQL 被追加到了错误位置，导致 Oracle 报 `ORA-00936`。

## 目标

修复 Oracle ROWNUM 分页 SQL 的外层包裹逻辑，确保原 SQL 位于内层查询括号中。

## 范围

- 修复 `OraclePageConverter` 的无排序、无分组、非第一页分页分支。
- 增加不依赖真实 Oracle 数据库的 SQL 拼接回归测试。

## 非目标

- 不调整 Oracle `OFFSET/FETCH` 分页逻辑。
- 不调整带排序或分组的 Oracle 分页逻辑。

## 关键设计

先保存当前构建完成的原 SQL，再清空 SQL 构建器，按外层查询、内层原 SQL、跳过行数条件的顺序重新组装。

## 验收标准

- 非第一页 ROWNUM 分页 SQL 不出现 `SELECT SELECT`。
- 原 SQL 位于 `FROM ( ... )` 内部。
- 回归测试通过。

## 任务清单

### 分析

- [x] 明确异常 SQL 与触发条件
- [x] 确认分页转换器影响范围

### 实施

- [x] 修复 Oracle ROWNUM 分页 SQL 拼接
- [x] 增加回归测试

### 验证

- [x] 执行相关单元测试
- [x] 检查生成 SQL 与变更 diff

### 收尾

- [x] 更新当前进度
- [x] 记录剩余风险与阻塞

## 当前进度

已完成 Oracle ROWNUM 分页 SQL 拼接修复，回归测试已通过，变更 diff 已检查。

## 风险与阻塞

暂无代码层面的阻塞。未连接真实 Oracle 数据库执行集成测试，实际数据库兼容性仍依赖现有 Oracle 集成环境。
