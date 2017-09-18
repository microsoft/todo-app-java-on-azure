# Todo App Java On Azure

## Requirements

* Azure Document DB
* JDK 1.8 and above
* Maven 3.0 and above

## Configuration

* Modify `src/main/resources/application.properties` file and save it

    ``` txt
    azure.documentdb.uri=put-your-documentdb-uri-here
    azure.documentdb.key=put-your-documentdb-key-here
    azure.documentdb.database=put-your-documentdb-databasename-here
    ``` 

## Run it

1. `mvn package`
1. `java -jar target/todo-app-java-on-azure-0.0.1-SNAPSHOT.jar`

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
