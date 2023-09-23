# LiquiBase Installation

1. Add dependency in the pom.xml/build.gradle
2. Check that spring:jpa:hibernate:ddl-auto: = validate
3. Check that db.changelog-maser.yaml is created in resources/db/changelog
4. Check that db.changelog-maser.yaml points to some file.sql

# Important things

- Liquibase creates tables only once