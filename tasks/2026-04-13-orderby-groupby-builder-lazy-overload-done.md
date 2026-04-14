# 任务背景

用户希望为 `OrderBy.OrderBuilder` 与 `GroupBy.GroupBuilder` 增加与条件构造器类似的延迟构造重载，避免在 `sure=false` 时提前执行依赖外部参数的 `field(...)` 表达式。

# 需求

- 为 `OrderBy` builder 添加 `add(boolean, Consumer<OrderBuilder>)` 重载
- 为 `GroupBy` builder 添加 `add(boolean, Consumer<GroupBuilder>)` 重载
- 在现有 MySQL 查询测试中补充最小验证
- 分析还有哪些 builder 需要类似重载

# 任务列表

- [x] 分析 `OrderBy` 与 `GroupBy` 当前 builder 结构
- [x] 为两个 builder 增加延迟构造重载
- [x] 在 MySQL 查询测试中补充 `sure=false` 场景验证
- [x] 梳理其他需要类似重载的 builder
- [x] 完成最小验证

# 完成进度

- 已为 `OrderBy.OrderBuilder` 增加 `add(boolean, Consumer<OrderBuilder>)`
- 已为 `GroupBy.GroupBuilder` 增加 `add(boolean, Consumer<GroupBuilder>)`
- 已在 MySQL 查询测试中补充 `groupBy/orderBy` 的延迟构造验证
- 已梳理出 `Select.EzSelectBuilder`、`UpdateSet.UpdateSetBuilder`、`Function.FunctionBuilder`、`WindowFunction.WindowFunctionBuilder` 也存在类似适配价值
