# Todo App Java On Azure

This TodoList app uses Azure DocumentDB spring boot starter and AngularJS to interact with Azure.

This branch show how to deploy the java project to kubernetes cluster using maven plugin.
It firstly uses spotify maven plugin to builds a docker image and push the image to a private Azure Container Registry.
Then, it uses fabric8 maven plugin to generate kubernetes resource yaml file and apply the yaml file to your cluster.

## Requirements

* Azure Cosmos DB DocumentDB([create one](https://docs.microsoft.com/en-us/azure/cosmos-db/create-documentdb-java))
* Azure Container Service with Kubernetes cluster ([create one](https://docs.microsoft.com/en-us/azure/container-service/kubernetes/container-service-kubernetes-walkthrough))
* Azure Container Registry([cresate one](https://docs.microsoft.com/en-us/azure/container-service/kubernetes/container-service-tutorial-kubernetes-prepare-acr))
* [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) 1.8 and above
* [Maven](https://maven.apache.org/) 3.0 and above

## Configuration

* Modify `src/main/resources/application.properties` file and save it

    ``` txt
    azure.documentdb.uri=put-your-documentdb-uri-here
    azure.documentdb.key=put-your-documentdb-key-here
    azure.documentdb.database=put-your-documentdb-databasename-here
    ``` 
    
* Modify `~/.m2/settings.xml` to add server information

    ```xml
    <server>
      <id>put-your-docker-registry-url</id>
      <username>put-your-docker-username</username>
      <password>put-your-docker-key</password>
      <configuration>
        <email>put-your-email</email>
      </configuration>
    </server>
    ```
    
* Modify `pom.xml`'s properties field

    ```xml
    <docker.image.prefix>put-your-docker-registry-url</docker.image.prefix>
    ```
    

## Run it

1. `mvn package`
1. Build docker image and push it

    ```bash
    mvn docker:build docker:push
    ```
1. Deploy the image to your kubernetes cluster

    ```bash
    mvn fabric8:resource fabric8:apply
    ```
 
1. Wait about one minutes to deploy. Get the service external url by running:

    ```bash
    kubectl get svc
    ```
    
    > If the deployment hasn't finished yet, the external url field will show `pending`
    
1. Open external url obtained in last you can see the web pages to show the todo list app

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
