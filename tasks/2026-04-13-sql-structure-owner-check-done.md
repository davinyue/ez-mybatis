# 任务背景

用户希望将表归属校验从 `Where` 扩展到整条语句结构，覆盖 `EzQuery`、`EzUpdate`、`EzDelete` 中的 `where/select/groupBy/having/orderBy/set`。

# 需求

- 基于主表和 join 表实例做统一作用域校验
- 判断规则按同一个 `Table` 对象实例
- 覆盖 `where/select/groupBy/having/orderBy/set`
- 保持最小必要修改，并做最小验证

# 任务列表

- [x] 分析 `Where/Select/GroupBy/Having/OrderBy/UpdateSet` 结构
- [x] 设计统一表作用域解析与递归校验方案
- [x] 实现 `EzQuery/EzUpdate/EzDelete` 构建期统一校验
- [x] 补充 core 层最小单元测试
- [x] 完成验证

# 完成进度

- 已新增统一表作用域解析器与 SQL 结构归属校验器
- 已在 `EzQuery/EzUpdate/EzDelete` 的 `build()` 阶段接入校验
- 已覆盖 `where/select/groupBy/having/orderBy/set` 的主表与 join 表实例校验
- 已补充不依赖数据库的 core 单元测试
- 已扩充为合法/非法场景矩阵测试，覆盖 query/update/delete 与 join on 等边界
- 已按 query/update/delete 拆分测试类，降低后续维护成本
- 已修正关联子查询（correlated subquery）场景的校验时机，避免 EXISTS 子查询在独立 build / 嵌套转 SQL 时被误判
