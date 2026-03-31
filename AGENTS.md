# Repository Guidelines

## Project Structure & Module Organization

This repository is a Maven multi-module Java 8 project.

- `ez-mybatis-core`: Core runtime code, SQL execution logic, DSL definitions, and dialect providers.
- `ez-mybatis-define`: Shared annotations, interfaces, and enums.
- `ez-mybatis-expand`: Optional SQL extensions.
- `ez-mybatis-to-plus`: MyBatis-Plus adapters and compatibility layer.
- `ez-mybatis-demo`: Integration examples, MyBatis XML configurations (`src/main/resources`), and executable
  cross-database test suites.

## Architecture & Code Design Patterns

- **Fluent API & Builder Pattern**: The project heavily relies on Lambda DSL (`EzQuery`, `EzUpdate`, `EzDelete`) for SQL
  construction. When adding functionalities, maintain chainable and semantic APIs.
- **Generic Components**: Avoid redundant syntactic sugar and combinatorial method overloads. Use `Operand`-based or
  semantic `add()`/`set()` APIs for SQL structure components (`Function`, `CaseWhen`, `GroupBy`, `OrderBy`,
  `WindowFunction`) to decouple them from specific tables or fields.
- **Cross-Database Abstraction**: Ez-MyBatis supports MySQL, Oracle, PostgreSQL, MS SQL Server, and DM. When introducing
  new SQL syntax (like window functions, exists), ensure the behavior is properly abstracted via dialect converters. Be
  extremely mindful of database-specific behaviors like case sensitivity of identifiers, limitation syntax, and function
  availability.
- **Simplicity & First Principles**: Revert to first principles to analyze problems. Keep implementations simple and
  maintainable. Avoid over-engineering defensive boundary conditions unless necessary for core stability.

## Build, Test, and Development Commands

Use Maven from the repository root.

- `mvn clean package -Dmaven.test.skip=true` builds all modules; this matches the GitHub publish workflow.
- `mvn test` runs unit and integration tests across modules.
- `mvn -pl ez-mybatis-demo -am test` runs the demo module and any required dependencies.
- `mvn -q -pl ez-mybatis-demo -am "-Dtest=org.rdlinux.oracle.OracleComplexEntityDeleteTest" test` runs a single
  database-specific test class.

## Coding Style & Naming Conventions

- **Formatting & Syntax**: Follow the existing Java 8 style: 4-space indentation, UTF-8 sources, one top-level public
  class per file. Ensure accurate package imports for all externally referenced classes.
- **Naming**: Keep package names lowercase under `org.rdlinux`. Use `UpperCamelCase` for classes, `lowerCamelCase` for
  methods/fields. Suffix database-specific implementations clearly, e.g., `MySqlDialectProvider` or
  `OracleMergeConverter`.
- **Lombok**: Widely used in the project. When adding entities or DTOs, stay consistent with existing annotations such
  as `@Getter`, `@Setter`, and `@FieldNameConstants`.

## Testing Guidelines

Tests use JUnit 4. Most behavioral coverage is integration-style and lives under `ez-mybatis-demo/src/test/java`,
grouped by database (`mysql`, `oracle`, `pg`, `mssql`, `dm`).

- **Base Test Class**: Test classes follow the `*Test` naming pattern and MUST extend the shared `AbstractBaseTest` to
  inherit unified lifecycle management and deterministic test data handling.
- **Assertions**: Normalize assertions. Use precise, business-level validations over non-idiomatic or superficial
  `assertNotNull` checks.
- **Integration Setup**: The checked-in MyBatis configs point at real database instances. Update local connection
  settings before running cross-database suites. Any database-specific dialect modifications must be backed by
  successfully passing its target database test suite.

## Commit & Pull Request Guidelines

Recent history uses short Conventional Commit prefixes, mainly `feat:`, `fix:`, `refactor:`, `test:`, and `docs:`. Keep
commit subjects brief and scoped to one change (e.g., `feat: add PostgreSQL merge test`). For pull requests:

- Include the affected modules, the database dialects touched, and the Maven command used for verification.
- Describe any required schema or config changes.
- If behavior differs by database, call that out explicitly in the description.
