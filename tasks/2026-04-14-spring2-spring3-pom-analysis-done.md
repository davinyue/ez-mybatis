# 任务背景

分析同级项目 `../ez-mybatis-spring2-project` 和 `../ez-mybatis-spring3-project` 的各个 Maven 模块，判断是否需要更新 `pom.xml` 配置。

# 需求

- 梳理两个项目的父子模块结构
- 检查各模块 `pom.xml` 的版本、依赖管理、插件配置和发布配置
- 判断哪些配置需要更新，哪些可以暂不调整
- 做最小必要验证

# 任务列表

- [x] 查看 spring2 项目父 POM 和 3 个模块 POM
- [x] 查看 spring3 项目父 POM 和 3 个模块 POM
- [x] 对比当前主仓库根 POM 的公共配置
- [x] 执行最小必要验证
- [x] 输出分析结论

# 完成进度

- 已完成 POM 结构分析
- 已执行 `mvn -q -f ../ez-mybatis-spring2-project/pom.xml validate -DskipTests`
- 已执行 `mvn -q -f ../ez-mybatis-spring3-project/pom.xml validate -DskipTests`
- 两个项目的 `validate` 均通过

# 结论摘要

- `ez-mybatis-spring2-project` 当前没有明显的构建级阻塞问题，但存在若干建议性更新点，主要是版本和公共配置未集中管理。
- `ez-mybatis-spring3-project` 存在更明确的更新必要，尤其是项目版本仍为 `1.0.3.RS`，而对 `ez-mybatis-core`、`ez-mybatis-to-plus` 的依赖直接跟随 `${project.version}`，与主仓库当前 `1.0.4.RS` 已不一致。
