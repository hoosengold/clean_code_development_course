### Table of contents

1. [Reference Documentation](#reference-documentation)
2. [Guides](#guides)
3. [Style Guides](#style-guides)
4. [Useful Commands](#useful-commands)

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.2/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.2/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#web)
* [Spring Data JDBC](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#data.sql.jdbc)
* [Spring Security](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#web.security)
* [JDBC API](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#data.sql)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Using Spring Data JDBC](https://github.com/spring-projects/spring-data-examples/tree/master/jdbc/basics)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Accessing Relational Data using JDBC with Spring](https://spring.io/guides/gs/relational-data-access/)
* [Managing Transactions](https://spring.io/guides/gs/managing-transactions/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

### Style Guides

- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)

### Useful Commands

- `mvn clean` - clear the compiled files
- `mvn compile` - compile the project
- `mvn test` - run all tests
- `mvn clean package` - creates a runnable jar file after all tests are successful
- `mvn clean package -DskipTests` - creates a runnable jar file without prior testing
- `java -jar target/backend-0.0.1-SNAPSHOT.jar` - start the program
- `java -Dspring.profiles.active=datagen -jar target/backend-0.0.1-SNAPSHOT.jar` - start the program and add initial test data