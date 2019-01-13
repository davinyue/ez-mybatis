# README

## 简介：

该项目旨在提供一个对dao层通用的操作，支持普通java程序和spring程序

插入支持：支持指定主键生成模式，uuid，自增，自指定；支持枚举指定使用名称还是序列。

删除支持：直接删除实体支持，主键删除支持。

更新支持：全替换更新支持，非空字段更新支持。

查询支持：连接查询，自定义条件查询，自定义sql查询，主键count查询，关联实体懒加载。

## 特性：

1. 关键字转义，防止sql注入；
2. 注解支持，对原有代码改动小；
3. 已经可以完成大部分的crud操作，但如统计类需求需要一次加载多表数据的，为了提高性能，用户可以使用自己的mybatis实现。
4. 支持深层次的join查询，支持排序，用户可指定where条件操作符，where条件链接符\(and,or\)，由于分页的原因暂不支持实体的关联加载，后续将支持懒加载。
5. 实体字段与数据库字段的映射默认是驼峰转下划线，可通过注解指定数据库的列名。

#### 使用示例

[项目地址请点击](https://github.com/linuxprobe-org/java-project-demo)

[代码实现请点击](https://github.com/linuxprobe-org/java-project-demo/blob/master/src/test/java/org/linuxprobe/demo/MybatisTest.java)

