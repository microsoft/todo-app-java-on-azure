# Todo App Java On Azure

This TodoList app is an Azure Java SpringBoot application. It provides end-to-end CRUD operations on todo list items from front-end AngularJS code. Behind the scenes, the todo list item data store is [Azure CosmosDB DocumentDB](https://docs.microsoft.com/en-us/azure/cosmos-db/documentdb-introduction). This application uses [Azure CosmosDB DocumentDB Spring Boot Starter](https://github.com/Microsoft/azure-spring-boot/tree/master/azure-starters/azure-documentdb-spring-boot-starter) and AngularJS to interact with Azure. In particular, this version of the application shows how to use [Azure Application Insights](https://docs.microsoft.com/en-us/azure/application-insights/app-insights-overview) to monitor the performance and usage of a Java SpringBoot application. For more information on how to use Application Insights in other types of Java applications, refer to [Get started with Application Insights in a Java web project] (https://docs.microsoft.com/en-us/azure/application-insights/app-insights-java-get-started).

## Requirements
* [Azure Cosmos DB DocumentDB](https://docs.microsoft.com/en-us/azure/cosmos-db/create-sql-api-java)
* [Azure App Insights](https://docs.microsoft.com/en-us/azure/application-insights/app-insights-create-new-resource)
* JDK 1.8 and above
* Maven 3.0 and above
* Clone ai branch

## Configuration

* Follow the steps for creating the Azure App Insights resource (as shown in the Requirements section) and copy the instrumentation key.
  Paste the instrumentation key into the 'src/main/resources/ApplicationInsights.xml' file and save it.
  
  <!-- The key from the portal: -->
      <InstrumentationKey>put-your-instrumentation-key-here</InstrumentationKey>
 
* In addition, you may notice that the following has already been configured for you to setup App Insights:
1. The dependency to Application Insights has been added to the pom.xml:
   <dependency>
            <groupId>com.microsoft.azure</groupId>
            <artifactId>applicationinsights-web</artifactId>
             <version>2.0.0-BETA</version>
    </dependency>

2. The 'src/main/java/com/microsoft/azure/sample/AppInsightsConfig.java' file has been added to register the HTTP filter using SpringBoot's FilterRegistrationBean.  This results in each of the HTTP requests being logged to Application Insights.  To monitor the telemetry gathered by Application Insights, open your Application Insights resource using the Azure Portal.
