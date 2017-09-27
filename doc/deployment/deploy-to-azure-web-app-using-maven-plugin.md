# Deploy to Azure Web App for Containers using Maven Plugin

This tutorial shows you how to deploy this containerized app to Azure Web App for Containers using [Maven Plugin for Azure Web Apps](https://github.com/Microsoft/azure-maven-plugins/tree/master/azure-webapp-maven-plugin).
Below are the major steps in this tutorial.
- [Create Azure Container Registry](#create-acr)
- [Setup and Customize Configuration](#config)
- [Build and Deploy Docker Container Image to Azure Web App for Containers](#deploy)
- [Clean Up Resources](#clean-up)

<a name="create-acr"></a>
## Create Azure Container Registry

1. login your Azure CLI, and set your subscription id 
    
   ```bash
   az login
   az account set -s <your-subscription-id>
   ```

1. Run below command to create an Azure Container Registry.
After creation, use `login server` as Container Registry URL in the next section.

   ```bash
   az acr create -n <your-registry-name> -g <your-resource-group-name> --sku <sku-name>
   ```
       where `<sku-name>` is one of the following: `{Basic,Managed_Basic,Managed_Standard,Managed_Premium}`.

1. Run below command to show your Azure Container Registry credentials.
You will use Docker registry username and password in the next section.

    ```bash
    az acr credential show -n <your-registry-name>
    ```

<a name="config"></a>
## Setup and Customize Configuration

1. In the Maven settings file `~/.m2/settings.xml`, add a new `<server>` element with your Azure Container Registry credentials from previous steps.

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

1. In `<properties>` section of the project's `pom.xml`, replace value in `<docker.image.prefix>` element with your Azure Container Registry URL.

    ```xml
    <docker.image.prefix>put-your-docker-registry-url</docker.image.prefix>
    ```

1. Optional. In `<properties>` section of the project's `pom.xml`, modify value in `<azure.app.name>` element with any unique string as your app name.

   Default value is `todo-app-${maven.build.timestamp}`. The `${maven.build.timestamp}` part is used to avoid conflict in Azure and will be different in each build. 

<a name="deploy"></a>
## Build and Deploy Docker Container Image to Azure Web App for Containers
1. Verify you can run your project successfully in your local environment. ([Run project on local machine](../../README.md))

1. Build project and docker container image, and push the image to your Azure Container Registry.

    ```bash
    mvn clean package docker:build docker:push
    ```

1. Deploy Docker container image from your Azure Container Registry to Azure Web App for Containers.

    ```bash
    mvn azure-webapp:deploy
    ```

    > NOTE: You can also run above commands as a one-liner:  
    > `mvn clean package docker:build docker:push azure-webapp:deploy`

1. Navigate to the website from your favorite browser.
You will see this app successfully running on Azure Web App for Containers.

<a name="clean-up"></a>
## Clean Up Resources

Delete the Azure resources you just created by running below command:

```bash
az group delete -y --no-wait -n <your-resource-group-name>
```
