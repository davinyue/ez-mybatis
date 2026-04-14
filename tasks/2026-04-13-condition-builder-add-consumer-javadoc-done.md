# 任务背景

用户希望补充 `ConditionBuilder#add(boolean, Consumer<SonBuilder>)` 的 JavaDoc，明确该重载用于延迟构造条件，避免在 `sure` 判定所依赖参数为空时，条件表达式提前求值导致异常。

# 需求

- 更新目标重载的 JavaDoc 注释
- 说明该重载与直接传入已构造 `Condition` 的区别
- 结合典型场景说明其用途

# 任务列表

- [x] 查看目标方法签名及现有注释
- [x] 更新 JavaDoc，补充延迟构造语义与示例
- [x] 在现有 MySQL 查询测试中补充该重载的行为验证
- [x] 完成最小验证

# 完成进度

- 已更新 `ConditionBuilder#add(boolean, Consumer<SonBuilder>)` 的 JavaDoc
- 已补充该重载用于避免 `sure=false` 时提前执行 `table.field(a)` 一类表达式的说明
- 已补充与 `add(boolean, Condition)` 的差异说明及示例
- 已在 MySQL 查询测试中补充 `sure=false` 时跳过 `table.field(null)` 延迟构造场景的验证
