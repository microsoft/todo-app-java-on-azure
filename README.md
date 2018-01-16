# Todo App Java On Azure

This TodoList app uses Azure DocumentDB spring boot starter and AngularJS to interact with Azure.

## Requirements

* Azure Cosmos DB DocumentDB([create one](https://docs.microsoft.com/en-us/azure/cosmos-db/create-documentdb-java))
* [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) 1.8 and above
* [Maven](https://maven.apache.org/) 3.0 and above
* Clone `aad-start` branch.

## Configuration

* Modify `src/main/resources/application.properties` file. You need to have an registered app in your Azure AD tenant and create a security key. The registered app needs to have `Access the directory as the signed-in user` and `Sign in and read user profile` delegated permissions. Put Application ID and Key in `clientId` and `clientSecret` respectively. List all the AAD groups in `ActiveDirectoryGroups` that you want to have Spring Security role objects mapped to them. The role objects can then be used to manage access to resources that is behind Spring Security.

    ``` txt
    azure.documentdb.uri=put-your-documentdb-uri-here
    azure.documentdb.key=put-your-documentdb-key-here
    azure.documentdb.database=put-your-documentdb-databasename-here
    azure.activedirectory.clientId=put-your-application-id-here
    azure.activedirectory.clientSecret=put-your-application-key-here
    azure.activedirectory.ActiveDirectoryGroups=group1,group2
    ```
* Add below dependencies in `pom.xml`.

    ``` txt
    <dependency>
        <groupId>org.springframework.security</groupId>^M
        <artifactId>spring-security-config</artifactId>^M
    </dependency>
    <dependency>
        <groupId>org.springframework.security.oauth</groupId>^M
        <artifactId>spring-security-oauth2</artifactId>^M
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>com.microsoft.azure</groupId>^M
        <artifactId>azure-ad-integration-spring-boot-starter</artifactId>^M
        <version>${azure.spring.boot.starter.version}</version>^M
    </dependency>
    ```

## Code

* Add `src/main/java/com/microsoft/azure/sample/security/WebSecurityConfig.java`
    ``` txt
    /**
     * Copyright (c) Microsoft Corporation. All rights reserved.
     * Licensed under the MIT License. See LICENSE in the project root for
     * license information.
     */
    package com.microsoft.azure.spring.boot.autoconfigure.aad.sample.security;

    import com.microsoft.azure.autoconfigure.aad.AADAuthenticationFilter;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
    import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
    import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

    @EnableOAuth2Sso
    @EnableGlobalMethodSecurity(securedEnabled = true,
            prePostEnabled = true)
    public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private AADAuthenticationFilter aadAuthFilter;

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http.authorizeRequests().antMatchers("/home").permitAll();
            http.authorizeRequests().antMatchers("/api/**").authenticated();

            http.logout().logoutSuccessUrl("/").permitAll();

            http.authorizeRequests().anyRequest().permitAll();

            http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

            http.addFilterBefore(aadAuthFilter, UsernamePasswordAuthenticationFilter.class);
        }
    }
    ```

* Modify `TodoListController.java` to enable role based access control (RBAC). The following example demonstrates restricting access to get requests to `/api/todolist` for users who are not in `group1`.

    ``` txt
    @PreAuthorize("hasRole('ROLE_group1')")
    @RequestMapping(value = "/api/todolist", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAllTodoItems() {
        ...
    ```

## Run it

1. `mvn package`
1. `java -jar target/todo-app-java-on-azure-0.0.1-SNAPSHOT.jar`
1. Open `http://localhost:8080` you can see the web pages to show the todo list app

## Contributing

This project welcomes contributions and suggestions.  Most contributions require you to agree to a
Contributor License Agreement (CLA) declaring that you have the right to, and actually do, grant us
the rights to use your contribution. For details, visit https://cla.microsoft.com.

When you submit a pull request, a CLA-bot will automatically determine whether you need to provide
a CLA and decorate the PR appropriately (e.g., label, comment). Simply follow the instructions
provided by the bot. You will only need to do this once across all repos using our CLA.

This project has adopted the [Microsoft Open Source Code of Conduct](https://opensource.microsoft.com/codeofconduct/).
For more information see the [Code of Conduct FAQ](https://opensource.microsoft.com/codeofconduct/faq/) or
contact [opencode@microsoft.com](mailto:opencode@microsoft.com) with any additional questions or comments.
