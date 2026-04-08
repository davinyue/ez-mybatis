# POM 公共配置上提

## 修改背景
- 当前父子模块 `pom.xml` 中存在较多重复的发布元数据、依赖版本和插件版本声明，维护成本偏高。
- 需要在不影响 Maven Central 发布的前提下，将适合继承的公共配置收敛到父 `pom`。

## 需求
- 将公共发布元数据统一保留在父 `pom`，子模块通过继承获取。
- 将通用依赖版本集中到父 `pom` 的 `dependencyManagement`。
- 将通用插件版本集中到父 `pom` 的 `pluginManagement`。
- 不上提 MyBatis-Plus 相关依赖，保留 `ez-mybatis-to-plus` 的模块特性边界。

## 任务列表
- [x] 梳理各模块中重复的发布元数据配置。
- [x] 将公共依赖版本上提到父 `pom` 的 `dependencyManagement`。
- [x] 将公共插件版本上提到父 `pom` 的 `pluginManagement`。
- [x] 保留 `ez-mybatis-demo` 的跳过发布与测试差异化配置。
- [x] 保留 `ez-mybatis-to-plus` 的 MyBatis-Plus 特有依赖声明。

## 完成进度
- 当前状态：已完成。
- 已完成：
  - 父 `pom` 新增 `mybatis`、`commons-lang3`、`junit`、`javax.persistence-api` 的统一版本管理。
  - 父 `pom` 新增 `lombok-maven-plugin` 与 `maven-deploy-plugin` 的统一插件版本管理。
  - 各子模块已移除重复的 `url`、`description`、`licenses`、`developers`、`scm` 配置，改为继承父 `pom`。
  - `ez-mybatis-core`、`ez-mybatis-define`、`ez-mybatis-demo` 已移除对应依赖的重复 `<version>` 声明。
  - `ez-mybatis-core`、`ez-mybatis-expand`、`ez-mybatis-demo` 已移除 `lombok-maven-plugin` 的重复版本声明。
  - `ez-mybatis-demo` 已改为继承 `maven-deploy-plugin` 版本，并继续保留 `<skip>true</skip>`。
  - `ez-mybatis-to-plus` 的 MyBatis-Plus 版本与依赖保持模块内声明，未上提到父 `pom`。
- 未完成：
  - 未执行完整 Maven 构建验证，当前仅完成配置层面的最小必要改动。
