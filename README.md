# Todo App Java On Azure

This TodoList app is an Azure Java application. It provides end-to-end CRUD operation to todo list item from front-end AngularJS code. Behind the scene, todo list item data store is [Azure CosmosDB DocumentDB](https://docs.microsoft.com/en-us/azure/cosmos-db/documentdb-introduction). This application uses [Azure CosmosDB DocumentDB Spring Boot Starter](https://github.com/Microsoft/azure-spring-boot/tree/master/azure-starters/azure-documentdb-spring-boot-starter) and AngularJS to interact with Azure. This sample application provides several deployment options to deploy to Azure, pls see deployment section below. With Azure support in Spring Starters, maven plugins and Eclipse/IntelliJ plugins, Azure Java application development and deployment is effortless now.


## TOC

* [Requirements](#requirements)
* [Create Azure Cosmos DB documentDB](#create-azure-cosmos-db-documentdb)
* [Configuration](#configuration)
* [Run it](#run-it)
* [Contribution](#contribution)
* Add new features
    * [Add AAD](https://github.com/Microsoft/todo-app-java-on-azure/tree/aad-start)
    * [Add KeyVault]()
* Deployment
    * [Deploy to Azure Web App for Containers using IntelliJ plugin](./doc/deployment/deploy-to-azure-web-app-using-intelliJ-plugin.md)
    * [Deploy to Azure Web App for Containers using Eclipse plugin](./doc/deployment/deploy-to-azure-web-app-using-eclipse-plugin.md)
    * [Deploy to Azure Container Service Kubernetes cluster using Maven plugin](./doc/deployment/deploy-to-azure-container-service-using-maven-plugin.md)
    * [Deploy to Azure Web App for Containers using Maven plugin](./doc/deployment/deploy-to-azure-web-app-using-maven-plugin.md)
* [Useful link](#useful-link)

## Requirements

* [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) 1.8 and above
* [Maven](https://maven.apache.org/) 3.0 and above

## Create Azure Cosmos DB documentDB

You can follow our steps using [Azure CLI 2.0](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli?view=azure-cli-latest) to deploy an Azure Cosmos DB documentDB,
or follow [this article](https://docs.microsoft.com/en-us/azure/cosmos-db/create-documentdb-java) to create it from Azure portal.

1. login your Azure CLI, and set your subscription id 
    
    ```bash
    az login
    az account set -s <your-subscription-id>
    ```
1. create an Azure Resource Group, and note your group name

    ```bash
    az group create -n <your-azure-group-name> -l <your-resource-group-region>
    ```

1. create Azure Cosmos DB with DocumentDB kind. Note the `documentEndpoint` field in the response.

   ```bash
   az cosmosdb create --kind GlobalDocumentDB -g <your-azure-group-name> -n <your-azure-documentDB-name>
   ```
   **Note** name of cosmos db must be in lowercase.
   
1. get your Azure Cosmos DB key, get the `primaryMasterKey` of the DocumentDB you just created.

    ```bash
    az cosmosdb list-keys -g <your-azure-group-name> -n <your-azure-documentDB-name>
    ```

## Configuration

* Note your DocumentDB uri and key from last step, specify a database name but no need to create it.
  Then modify `src/main/resources/application.properties` file and save it.

    ``` txt
    azure.documentdb.uri=put-your-documentdb-uri-here
    azure.documentdb.key=put-your-documentdb-key-here
    azure.documentdb.database=put-your-documentdb-databasename-here
    ``` 

* If you don't want to modify configuration in the source code manually, you can put variables in this file and 
  set their values in system environment variables: `DOCUMENTDB_URI`, `DOCUMENTDB_KEY` and `DOCUMENTDB_DBNAME`.
  Then maven will substitute them during the build phase.
    ``` txt
    azure.documentdb.uri=@env.DOCUMENTDB_URI@
    azure.documentdb.key=@env.DOCUMENTDB_KEY@
    azure.documentdb.database=@env.DOCUMENTDB_DBNAME@
    ``` 

## Run it

1. package the project using `mvn package`
1. Run the project using `java -jar target/todo-app-java-on-azure-1.0-SNAPSHOT.jar`
1. Open `http://localhost:8080` you can see the web pages to show the todo list app

## Clean up

Delete the Azure resources you created by running the following command:

```bash
az group delete -y --no-wait -n <your-resource-group-name>
```

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

## Useful link
- [Azure Spring Boot Starters](https://github.com/Microsoft/azure-spring-boot)
- [Azure Maven plugins](https://github.com/Microsoft/azure-maven-plugins)
