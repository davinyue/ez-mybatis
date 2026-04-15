# 项目优化项分析整理
## 修改背景
当前对 `ez-mybatis` 项目进行了静态分析，已初步识别出若干影响可维护性、隔离性、测试可执行性和构建治理的系统性问题。为便于后续逐项推进优化，需要先将当前已确认的问题与优先事项沉淀到 `tasks` 目录中。

## 需求
1. 将当前已分析出的 6 个优化点按任务规则记录到 `tasks` 目录。
2. 明确每个优化点对应的问题背景，便于后续拆分实施。
3. 保留当前结论中的优先级判断，作为后续改造顺序参考。
4. 本次仅记录任务，不直接修改业务代码或构建配置。

## 任务列表
- [x] 记录 `Configuration` 相关全局静态缓存导致的状态隔离与内存滞留问题。
- [x] 记录按 `Configuration` 设置方言时实际落为全局 `DbType` 覆盖的问题。
- [x] 记录开发模式下类变动热加载机制的实现方式及可优化点。
- [x] 记录测试体系对真实数据库和固定连接配置的强依赖问题。
- [x] 记录方言 `registerConverters()` 手工铺开、重复维护成本高的问题。
- [x] 记录 Maven 配置和依赖版本分散、重复声明较多的问题。

## 完成进度
- 当前状态：已开始进入代码改造阶段，优先落地热加载相关重构。
- 已完成：
  - 已整理并记录全局状态缓存问题：`EzMybatisContent`、`DefaultEzMybatisEntityInfoCache` 通过静态 Map 持有 `Configuration` 相关状态，存在隔离性不足和生命周期难以管理的问题。
  - 已整理并记录方言覆盖问题：`setProvider(Configuration, provider)` 最终仍写入全局 `DbType -> Provider` 映射，导致配置级覆盖实际升级为全局覆盖。
  - 已整理并记录热加载实现问题：当前热加载能力保留，后续优化方向不是取消，而是将监听逻辑从缓存实现中抽离；默认仍保持零配置体验，自动发现标准编译输出目录（如 `target/classes`、`target/test-classes`），同时补充外部热加载通知接口，兼容 IDEA HotSwap、Spring DevTools、JRebel 等外部机制主动通知缓存失效。
  - 已整理并记录测试体系问题：`ez-mybatis-demo` 的测试依赖真实数据库，多个 `mybatis-config-*.xml` 中存在固定 IP、账号和密码，导致大多数环境无法直接执行完整测试。
  - 已整理并记录方言注册问题：各数据库 `registerConverters()` 大量手工注册 converter，跨方言复用关系分散，存在复制式维护风险。
  - 已整理并记录构建治理问题：父子 `pom` 中插件声明重复，依赖与插件版本未充分集中管理，存在版本漂移和维护成本。
  - 已完成热加载第一阶段改造：将缓存存取与文件监听拆开，新增统一热加载通知接口，默认仍自动发现标准编译输出目录，并保留多模块项目的热加载支持目标。
  - 已完成方言覆盖问题的第一阶段修复：`DbDialectProvider` 已下沉到 `EzContentConfig`，`setProvider(Configuration, provider)` 现在会真正绑定到指定 `Configuration`，主要 SQL 生成链路、Mapper Provider、JDBC DAO、嵌套查询和 `insert by query` 等路径已切换为按配置获取 provider。
  - 已完成全局状态生命周期问题的第一阶段修复：新增 `EzMybatisContent.destroy(Configuration)`、`EzMybatisContent.destroyAll()` 以及实体信息缓存的按 `Configuration` 清理能力，为显式释放上下文缓存提供入口。
  - 已完成 `ez-mybatis-core` 模块编译验证，当前改造已通过 `mvn -pl ez-mybatis-core -DskipTests compile`。
  - 已执行全量测试验证：`mvn test` 可完成 `define`、`core`、`expand`、`to-plus` 模块构建，但 `demo` 模块集成测试失败，失败主因是测试环境无法建立真实数据库连接，不是本轮代码改造导致的编译错误。
  - 已完成 MySQL 集成测试验证：在放开网络限制后执行 `mvn -pl ez-mybatis-demo -am -Dtest=org.rdlinux.mysql.MySqlComplexEntityDeleteTest,org.rdlinux.mysql.MySqlComplexEntityInsertTest,org.rdlinux.mysql.MySqlComplexEntitySelectTest,org.rdlinux.mysql.MySqlComplexEntityUpdateTest,org.rdlinux.mysql.MySqlComplexEntityUpsertTest test`，共 98 个测试全部通过，说明当前改造在真实 MySQL 环境下未引入回归。
  - 已修正 `pg/mssql` merge 测试中的列名大小写假设问题：`columnNameCasePolicy` 仅作用于实体解析，不应改变用户通过 `sourceTable.column("...")` 显式指定的列名，因此将 `sourceTable.column("ID")` 按对应库的 `LOWER_CASE` 策略改为 `sourceTable.column("id")`。
  - 已完成 `pg` 与 `mssql` 的 merge 回归验证：`PgComplexMergeTest`、`MsSqlComplexMergeTests` 均已在真实数据库上执行通过，确认本次失败来自测试用例写法，不是框架大小写策略回归。
  - 已完成 Maven 公共配置收敛：将编码、Java 版本、编译/源码/javadoc/gpg/central 发布插件版本，以及 `lombok` 版本统一上提到父 `pom`；子模块仅保留差异化配置，`demo` 模块继续保留 `maven-deploy-plugin` 的跳过发布设置。
  - 已完成测试体系第一阶段治理：`ez-mybatis-demo` 默认跳过真实数据库集成测试，使根目录 `mvn test` 可直接执行通过；同时通过 `test` 属性激活和父 `pom` 的 Surefire 配置，保留了 `-Dtest=...` 直接执行目标数据库测试类的能力。
  - 已完成 `ez-mybatis-core` 基础测试修复：将原先仅包含 `main` 方法的演示类改成真正的 JUnit 4 断言测试，并补充 `junit` 测试依赖，当前 `FnFieldTest`、`MybatisParamHolderTest` 均已正常执行。
  - 已完成当前阶段回归验证：
    - `mvn -pl ez-mybatis-core,ez-mybatis-demo -am -DskipTests compile` 通过。
    - `mvn test` 在默认配置下通过，`demo` 模块数据库集成测试已按预期跳过。
    - `mvn -pl ez-mybatis-demo -am -Dtest=org.rdlinux.pg.PgComplexMergeTest test` 通过，说明显式指定测试类时数据库测试仍可直接运行。
  - 已完成 `dm` 方言转换器评估：`DmDialectProvider` 的 `registerConverters()` 与 `OracleDialectProvider` 基本同构，差异主要集中在 `Where/Join/From/Select/GroupBy/Page/Table/Update/Function/Formula/Union/Limit/SqlHint` 等达梦专用 converter，且 `ArgCompareArgCondition` 仍直接复用 `OracleArgCompareArgConditionConverter`。这说明 `dm` 方言提供器适合改为继承 `OracleDialectProvider`，在复用父类注册结果后再覆写达梦独有 converter。
  - 已完成 `dm` 方言提供器清理：`DmDialectProvider` 已改为继承 `OracleDialectProvider`，仅保留 `DmSelectConverter`、`DmPageConverter`、`DmLimitConverter` 这类达梦特有行为注册，其余改为直接复用 `Oracle*` 或通用 converter。
  - 已完成 `dm` 空壳 converter 清理：删除了一批仅包装 `Oracle*` / `MySql*` 实现、没有独立行为的 `Dm*Converter` 文件，降低了目录噪音和复制维护成本。
  - 已完成 `dm` 方言回归验证：
    - `mvn -pl ez-mybatis-core -DskipTests compile` 通过。
    - `mvn -pl ez-mybatis-demo -am -Dtest=org.rdlinux.dm.DmComplexEntityDeleteTest,org.rdlinux.dm.DmComplexEntityInsertTest,org.rdlinux.dm.DmComplexEntitySelectTest,org.rdlinux.dm.DmComplexEntityUpdateTest,org.rdlinux.dm.DmComplexMergeTest test` 通过，共 99 个测试全部通过。
  - 已完成 `oracle` converter 包装层分析：
    - 可直接删除并改为在 `OracleDialectProvider` 中直接注册 `MySql*` 的包装类：
      `OracleWhereConverter`、`OracleHavingConverter`、`OracleFromConverter`、`OracleOrderByConverter`、`OracleOrderItemConverter`、`OracleGroupByConverter`、`OracleDbTableConverter`、`OracleEntityTableConverter`、`OracleEzQueryTableConverter`、`OracleSqlTableConverter`、`OracleCaseWhenConverter`、`OracleSelectAllItemConverter`、`OracleSelectTableAllItemConverter`、`OracleUpdateColumnItemConverter`、`OracleUpdateFieldItemConverter`、`OracleSqlConditionConverter`、`OracleGroupConditionConverter`、`OracleFormulaConverter`、`OracleGroupFormulaElementConverter`、`OracleUnionConverter`、`OracleSqlHintConverter`。
    - 不应删除、仍需保留 Oracle 自有行为的 converter：
      `OracleSelectConverter`、`OraclePageConverter`、`OracleLimitConverter`、`OracleFunctionConverter`、`OracleArgCompareArgConditionConverter`、`OracleJoinConverter`、`OracleNormalPartitionConverter`、`OracleSubPartitionConverter`。
    - `OracleEzQueryConverter` 当前虽然只是在 `MySqlEzQueryConverter` 基础上改写了分页子查询包裹行为，但它确实承载 Oracle 差异，不应按纯包装类删除。
  - 已完成 `oracle` 方言提供器结构整理：`OracleDialectProvider` 已改为继承 `MySqlDialectProvider`，在复用 MySQL 默认注册结果的基础上，仅覆写 Oracle 真正有差异的 `Join`、`Select`、`Page`、`Limit`、`Function`、`ArgCompareArgCondition`、`EzQuery` 以及 Oracle 分区相关 converter，减少了 `registerConverters()` 中的重复注册代码。
  - 已完成 `dm` 方言提供器进一步精简：在 `DmDialectProvider` 继承 `OracleDialectProvider` 后，已不再重复注册 Oracle 侧 converter，仅覆写达梦自身差异的 `Select`、`Page`、`Limit` 三个 converter。
  - 已确认 Oracle 别名匹配问题根因：此前 Oracle 测试基类关闭了关键字引号，导致查询中显式指定的别名被数据库自动提升为全大写，进而与 `MapRetKeyCasePolicy.HUMP` 下的断言键不匹配；当前已通过在 `OracleBaseTest` 中重新开启关键字引号修正测试基线。
  - 已完成本轮回归验证：
    - `mvn -pl ez-mybatis-core -DskipTests compile` 通过。
    - `mvn -pl ez-mybatis-demo -am -Dtest=org.rdlinux.dm.DmComplexEntityDeleteTest,org.rdlinux.dm.DmComplexEntityInsertTest,org.rdlinux.dm.DmComplexEntitySelectTest,org.rdlinux.dm.DmComplexEntityUpdateTest,org.rdlinux.dm.DmComplexMergeTest test` 通过，共 99 个测试全部通过。
    - `mvn -pl ez-mybatis-demo -am -Dtest=org.rdlinux.oracle.OracleComplexEntityDeleteTest,org.rdlinux.oracle.OracleComplexEntityInsertTest,org.rdlinux.oracle.OracleComplexEntitySelectTest,org.rdlinux.oracle.OracleComplexEntityUpdateTest,org.rdlinux.oracle.OracleComplexMergeTest test` 通过，共 99 个测试全部通过。
  - 已补充 `core` 层针对性自动化测试：
    - 新增 `EzMybatisContentIsolationTest`，覆盖 `Configuration` 级方言 provider 隔离，以及 `destroy(configuration)` 只清理当前配置上下文、不影响其他配置的行为。
    - 新增 `HotReloadSupportTest`，覆盖实体信息热加载通知器对反射缓存的清理，以及标准编译输出目录解析器对多模块目录结构的扫描行为。
  - 已完成 `core` 测试回归验证：
    - `mvn -pl ez-mybatis-core test` 通过，共 7 个测试全部通过。
- 未完成：
  - 方言 `registerConverters()` 的重复整理目前已覆盖 `pg`、`mssql`、`oracle`、`dm`，后续可继续补针对性回归测试，而不是继续抽象层级。
  - 尚未补充更系统的回归测试策略，例如数据库矩阵执行约定与 CI 触发方式。

## 热加载方案补充
- 热加载能力本身继续保留，目标仍然是提升开发期用户体验。
- 后续建议将“缓存存取/失效”和“文件监听/线程管理”拆开，避免在缓存实现构造时隐式启动监听线程。
- 默认行为保持零配置，优先自动发现 Maven 多模块下常见的编译输出目录，不要求用户手工配置监听路径。
- 本次后续改造必须继续支持多模块项目，不能因为职责拆分而退化为仅支持单模块目录监听。
- 在自动监听之外，补充统一的外部热加载通知接口，使外部运行环境在类已热替换后可以直接通知框架清理 `ReflectionUtils` 与实体信息缓存。
- 推荐落地方向：
- 1. 保留默认自动监听标准编译目录。
- 2. 自动发现当前模块、子模块以及必要父级范围内的标准编译输出目录，保持多模块项目的热加载体验。
- 2. 新增统一缓存失效通知入口，作为文件监听与外部热替换机制的共用入口。
- 3. 为 IDEA HotSwap、Spring DevTools、JRebel 等场景预留扩展能力，但不把它们作为唯一依赖方案。
