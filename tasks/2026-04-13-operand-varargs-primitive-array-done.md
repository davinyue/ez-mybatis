# 任务背景

用户反馈 `Operand.in(Object... operands)` 在传入基础类型数组时会把整个数组识别成单个对象，导致 `IN` 条件未按预期展开。

# 需求

- 识别并修复 `field.in(int[])` 等基础类型数组经由 varargs 入口时的展开问题
- 保持最小必要修改
- 补充与本次改动直接相关的最小验证

# 任务列表

- [x] 分析 `Operand.valueToCollection` 与 `in/notIn` 的调用链
- [x] 修复 `Object...` 入口对单个数组参数的展开逻辑
- [x] 将基础类型数组场景整合到现有 MySQL `in` 查询测试
- [x] 完成最小验证

# 完成进度

- 已确认问题不在 `valueToCollection`，而在 `in(Object... operands)` 的 varargs 装箱路径
- 已新增 `valueToArgListFromVarargs`，在单参数且参数本身是数组或集合时按其内容展开
- 已将 `in/orIn/notIn/orNotIn` 四个 `Object...` 重载接入该逻辑
- 已将 `int[]` 传入 `in(...)` 的验证整合到现有 MySQL `in` 查询测试，未保留额外独立测试文件
