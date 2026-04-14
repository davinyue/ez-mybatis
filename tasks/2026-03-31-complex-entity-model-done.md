# 完善测试实体模型与全库兼容支持
## 修改背景
当前 `ez-mybatis-demo` 的测试数据模型（如 `User`、`Org`、`UserOrg`）相对简单，难以覆盖框架在复杂实体、类型转换、事件监听、TypeHandler、多表关联以及跨数据库兼容场景下的真实行为。为提高回归测试质量，需要建立一套更完整的复杂实体测试模型，并将主要数据库方言下的测试迁移到这套新模型上。

## 需求
1. 建立复杂实体测试模型，覆盖常见基础类型、枚举、日期、二进制、大文本、JSON 映射等场景。
2. 支持基于事件监听器的数据改写与基于 TypeHandler 的自定义对象映射测试。
3. 建立多表关联与嵌套查询所需的实体关系，用于验证 Join、嵌套查询、批量操作等能力。
4. 为 MySQL、PostgreSQL、Oracle、MsSQL、DM 提供对应的初始化建表脚本与测试用例。
5. 完成 MySQL 及其它数据库方言下复杂实体 Insert / Delete / Update / Select / Merge 或 Upsert 测试迁移。

## 任务列表
- [x] 新建复杂测试主实体 `ComplexUser`，补齐多种基础类型与扩展字段映射。
- [x] 创建 `ExtInfoJsonTypeHandler`，支持 JSON 与对象的双向映射测试。
- [x] 调整 `secretContent` 相关测试方式，明确监听器与 TypeHandler 的职责边界。
- [x] 新增 `ComplexDepartment`、`ComplexOrder`、`ComplexOrderItem` 等关联实体。
- [x] 统一补齐新增模型与字段的 JavaDoc 注释。
- [x] 产出 MySQL / PostgreSQL / Oracle / MsSQL / DM 的 `schema-*.sql` 初始化脚本。
- [x] 搭建复杂实体测试骨架并补齐加解密、JSON、自定义字段等验证能力。
- [x] 完成 MySQL 复杂实体 Insert 测试迁移。
- [x] 完成 MySQL 复杂实体 Delete 测试迁移。
- [x] 完成 MySQL 复杂实体 Update 测试迁移。
- [x] 完成 MySQL 复杂实体 Select 测试迁移。
- [x] 完成 MySQL 复杂实体 Upsert 测试迁移。
- [x] 增强 `Where`、`Join`、`Having` 等 Lambda 构建能力，补齐测试支撑。
- [x] 迁移 Oracle、PostgreSQL、MsSQL、DM 的复杂实体测试用例，并根据各方言差异完成修正。
- [x] 修复 MsSQL `JdbcUpdateDao replace` 场景下空值参数 JDBC 类型推断问题。
- [x] 迁移 `DmMergeTest` 到新复杂实体模型。
- [x] 完成任务文档收尾与进度记录。

## 完成进度
已全部完成。

本次任务已经完成复杂实体测试模型的设计、建表脚本补齐、MySQL 全量复杂实体测试迁移，以及 Oracle / PostgreSQL / MsSQL / DM 的复杂实体测试迁移与兼容修复。期间还补齐了方言差异导致的测试问题，包括 Oracle/DM 列名大小写、非 MySQL 单表删除限制、Oracle 时间 SQL 写法、MsSQL JDBC replace 空值类型推断，以及 DM merge 场景迁移到新模型。

当前可以将复杂实体测试体系视为新的主测试模型基线，后续新增数据库能力或 SQL 方言特性时，可以直接在这套模型上扩展验证。
