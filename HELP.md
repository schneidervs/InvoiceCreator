# Read Me First
The following was discovered as part of building this project:

* The original package name 'com.github.schneidervs.InvoiceСreator' is invalid and this project uses 'com.github.schneidervs.Invoice.reator' instead.

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/4.0.0-SNAPSHOT/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/4.0.0-SNAPSHOT/gradle-plugin/packaging-oci-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/4.0.0-SNAPSHOT/reference/web/servlet.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/4.0.0-SNAPSHOT/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Spring Security](https://docs.spring.io/spring-boot/4.0.0-SNAPSHOT/reference/web/spring-security.html)
* [Thymeleaf](https://docs.spring.io/spring-boot/4.0.0-SNAPSHOT/reference/web/servlet.html#web.servlet.spring-mvc.template-engines)
* [JTE](https://jte.gg/)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/4.0.0-SNAPSHOT/reference/using/devtools.html)
* [Java Mail Sender](https://docs.spring.io/spring-boot/4.0.0-SNAPSHOT/reference/io/email.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Handling Form Submission](https://spring.io/guides/gs/handling-form-submission/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

## JTE

This project has been configured to use [JTE precompiled templates](https://jte.gg/pre-compiling/).

However, to ease development, those are not enabled out of the box.
For production deployments, you should remove

```properties
gg.jte.development-mode=true
```

from the `application.properties` file and set

```properties
gg.jte.use-precompiled-templates=true
```

instead.
For more details, please take a look at [the official documentation](https://jte.gg/spring-boot-starter-3/).

для тестов указываем
@ActiveProfiles("test")
@SpringBootTest
public class YourTestClass {
// ...
}
