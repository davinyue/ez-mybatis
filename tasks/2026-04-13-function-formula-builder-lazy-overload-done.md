# 任务背景

用户要求优先为 `Function` 与 `Formula` 补充延迟构造能力，避免在 `sure=false` 时提前执行依赖外部参数的 `field(...)` 表达式。

# 需求

- 为 `Function` builder 增加延迟构造重载
- 为 `Formula` builder 增加最小必要的延迟构造重载
- 在现有 MySQL 查询测试中补充验证
- 保持最小必要修改

# 任务列表

- [x] 分析 `Function` 与 `Formula` 的 builder 结构和适配边界
- [x] 为 `Function` 增加延迟构造重载
- [x] 为 `Formula` 的元素 builder 增加延迟构造重载
- [x] 在 MySQL 查询测试中补充验证
- [x] 完成最小验证

# 完成进度

- 已为 `Function.FunctionBuilder` 增加 `addArg(boolean, Consumer<FunctionBuilder>)`
- 已为 `Function.FunctionBuilder` 增加 `addDistinctArg(boolean, Consumer<FunctionBuilder>)`
- 已为 `Formula.FormulaEleBuilder` 增加 `add/subtract/multiply/divide` 的延迟构造回调重载
- 已在 MySQL `ezQueryFunctionsAndFormulas` 测试中补充 `sure=false` 跳过 `table.field(null)` 场景
- 未对 `FormulaStartBuilder.with(...)` 增加条件重载，避免在公式起始元素缺失时形成不完整结构
