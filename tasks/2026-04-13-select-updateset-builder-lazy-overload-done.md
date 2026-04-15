# 任务背景

用户要求继续为 `SelectBuilder` 与 `UpdateSetBuilder` 增加延迟构造重载，以避免在 `sure=false` 时提前执行依赖外部参数的字段构造表达式。

# 需求

- 为 `Select.EzSelectBuilder` 增加延迟构造重载
- 为 `UpdateSet.UpdateSetBuilder` 增加延迟构造重载
- 补充与现有测试组织一致的最小验证

# 任务列表

- [x] 分析 `SelectBuilder` 与 `UpdateSetBuilder` 现有入口
- [x] 增加延迟构造重载
- [x] 为 `SelectBuilder` 增补 MySQL 查询测试
- [x] 为 `UpdateSetBuilder` 增补 core 层最小测试
- [x] 完成最小验证

# 完成进度

- 已为 `Select.EzSelectBuilder` 增加 `add(boolean, Consumer<EzSelectBuilder>)`
- 已为 `UpdateSet.UpdateSetBuilder` 增加 `add(boolean, Consumer<UpdateSetBuilder>)`
- 已在 MySQL `ezQueryBasicSelect` 中补充 `sure=false` 跳过 `table.field(null)` 场景
- 已在 `EzUpdateTest` 中补充 `UpdateSetBuilder` 延迟构造测试
