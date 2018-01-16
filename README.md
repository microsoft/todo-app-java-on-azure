# Todo App Java On Azure

This TodoList app is an Azure Java application. It provides end-to-end CRUD operation to todo list item from front-end AngularJS code. Behind the scene, todo list item data store is [Azure CosmosDB DocumentDB](https://docs.microsoft.com/en-us/azure/cosmos-db/documentdb-introduction). This application uses [Azure CosmosDB DocumentDB Spring Boot Starter](https://github.com/Microsoft/azure-spring-boot/tree/master/azure-starters/azure-documentdb-spring-boot-starter) and AngularJS to interact with Azure. In particular, this version of the application shows how to use [Azure Application Insights](https://docs.microsoft.com/en-us/azure/application-insights/app-insights-java-get-started) to monitor the performance and usage of the application.

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
             <version>[1.0,)</version>
    </dependency>

2. The 'src/main/java/com/microsoft/azure/sample/AppInsightsConfig.java' file has been added to register the HTTP filter using SpringBoot's FilterRegistrationBean.  This results in each of the HTTP requests to be logged to Application Insights.
