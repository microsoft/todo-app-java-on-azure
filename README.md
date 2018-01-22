# Todo App Java On Azure

This TodoList app is an Azure Java SpringBoot application. It provides end-to-end CRUD operations on todo list items from front-end AngularJS code. Behind the scenes, the todo list item data store is [Azure CosmosDB DocumentDB](https://docs.microsoft.com/en-us/azure/cosmos-db/documentdb-introduction). This application uses [Azure CosmosDB DocumentDB Spring Boot Starter](https://github.com/Microsoft/azure-spring-boot/tree/master/azure-starters/azure-documentdb-spring-boot-starter) and AngularJS to interact with Azure. In particular, this version of the application shows how to use [Azure Application Insights](https://docs.microsoft.com/en-us/azure/application-insights/app-insights-overview) to monitor the performance and usage of a Java SpringBoot application. For more information on how to use Application Insights in other types of Java applications, refer to [Get started with Application Insights in a Java web project](https://docs.microsoft.com/en-us/azure/application-insights/app-insights-java-get-started).

## Requirements
* [Azure Cosmos DB DocumentDB](https://docs.microsoft.com/en-us/azure/cosmos-db/create-sql-api-java)
* [Azure App Insights](https://docs.microsoft.com/en-us/azure/application-insights/app-insights-create-new-resource)
* [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) 1.8 and above
* [Maven](https://maven.apache.org/) 3.0 and above
* Clone ai branch

## Configuration
First, follow the instructions in the [master branch's ReadMe](https://github.com/Microsoft/todo-app-java-on-azure) to configure the app with Document DB.

Follow the steps for creating the Azure App Insights resource (as shown in the Requirements section) and copy the instrumentation key.
  Paste the instrumentation key into the 'src/main/resources/ApplicationInsights.xml' file and save it.
  ``` txt
  <!-- The key from the portal: -->
      <InstrumentationKey>put-your-instrumentation-key-here</InstrumentationKey>
 ```
In addition, you may notice that the following has already been configured for you to setup App Insights:
* The dependency to Application Insights has been added to the pom.xml:
   ``` txt
   <dependency>
            <groupId>com.microsoft.azure</groupId>
            <artifactId>applicationinsights-web</artifactId>
             <version>2.0.0-BETA</version>
    </dependency>
   ```
* The 'src/main/java/com/microsoft/azure/sample/AppInsightsConfig.java' file has been added to register the HTTP filter using SpringBoot's FilterRegistrationBean.  This results in each of the HTTP requests being logged to Application Insights.  

## Run it

1. `mvn package`
1. `java -jar target/todo-app-java-on-azure-0.0.1-SNAPSHOT.jar`
1. Open `http://localhost:8080` you can see the web pages to show the todo list app

To monitor the telemetry gathered by Application Insights, open your Application Insights resource using the Azure Portal.

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

