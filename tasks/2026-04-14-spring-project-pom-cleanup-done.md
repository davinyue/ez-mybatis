# 任务背景

用户已将 `ez-mybatis-spring2-project` 和 `ez-mybatis-spring3-project` 的版本统一更新为 `1.0.4.RS`，需要继续处理其余 POM 配置问题。

# 需求

- 清理多余或未使用的 POM 配置
- 收敛可统一管理的 Maven 版本配置
- 更新明显过旧的插件版本
- 保持最小必要修改

# 任务列表

- [x] 整理 spring2 父 POM 的公共属性和依赖管理
- [x] 清理 spring2 模块中的冗余版本属性
- [x] 更新 spring2、spring3 的 Lombok Maven 插件版本
- [x] 执行最小必要 Maven 验证
- [x] 记录结果

# 完成进度

- 已将 spring2 项目中的 Spring、MyBatis、Spring Boot 相关版本上收至父 POM `dependencyManagement`
- 已将两个项目父 POM 中的插件版本上收至 `properties`
- 已将两个项目的 `lombok-maven-plugin` 版本统一为 `1.18.20.0`
- 已移除 spring2 模块内未使用或可由父 POM 统一管理的版本声明
- 已执行 `mvn -q -f ../ez-mybatis-spring2-project/pom.xml validate -DskipTests`
- 已执行 `mvn -q -f ../ez-mybatis-spring3-project/pom.xml validate -DskipTests`

# 结果

- 两个项目的 `validate` 均通过
- 本次修改未调整模块依赖关系和排除规则，仅整理版本管理与插件配置
