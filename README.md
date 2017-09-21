# Todo App Java On Azure

This TodoList app uses Azure DocumentDB spring boot starter and AngularJS to interact with Azure.
It supports end-to-end CRUD operation to todo list item from front-end AngularJS code, and calls a Spring Restful API in backend.
Then the backend Java code uses Azure DocumentDB spring boot starter to interact with Azure.

## TOC

* [Requeirements](#requirements)
* [Create Azure Cosmos DB documentDB](#create-azure-cosmos-db-documentdb)
* [Configuration](#configuration)
* [Run it](#run-it)
* [Contribution](#contribution)
* Add new features
    * [Add AAD]()
    * [Add KeyVault]()
* Deployment
    * [Deploy to Azure Container Service Kubernetes cluster using maven plugin]()
    * [Deploy to Azure Web App using maven plugin]()

## Requirements

* [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) 1.8 and above
* [Maven](https://maven.apache.org/) 3.0 and above

## Create Azure Cosmos DB documentDB

You can follow our steps using [azure-cli](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli?view=azure-cli-latest) to deploy an Azure Cosmos DB documentDB,
or follow [this article](https://docs.microsoft.com/en-us/azure/cosmos-db/create-documentdb-java) to create it from azure portal.

1. login your azure cli, and set your subscription id 
    
    ```bash
    az login
    az account set -s <your-subscription-id>
    ```
1. create an Azure Resource Group, and note your group name

    ```bash
    az group create -n <your-azure-group-name>
    ```

1. create Azure Cosmos DB with DocumentDB kind. Note the `documentEndpoint` field in the response.

   ```bash
   az cosmosdb create --kind GlobalDocumentDB -g <your-azure-group-name> -n <your-azure-documentDB-name>
   ```
   
1. get your Azure Cosmos DB key, get the `primaryMasterKey` of the Document DB you just created.

    ```bash
    az cosmosdb list-keys
    ```

## Configuration

* Note your DocumentDB uri and key from last step, specify a database name but no need to create it.
  Then modify `src/main/resources/application.properties` file and save it.

    ``` txt
    azure.documentdb.uri=put-your-documentdb-uri-here
    azure.documentdb.key=put-your-documentdb-key-here
    azure.documentdb.database=put-your-documentdb-databasename-here
    ``` 

## Run it

1. package the project using `mvn package`
1. Run the project using `java -jar target/todo-app-java-on-azure-0.0.1-SNAPSHOT.jar`
1. Open `http://localhost:8080` you can see the web pages to show the todo list app

## Clean up

Delete the azure resource you created by running the following command:

```bash
az group delete --name <your-resource-group-name> --yes --no-wait
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
