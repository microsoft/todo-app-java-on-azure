# Deploy to Azure Web App for Containers using Eclipse Plugin

This tutorial shows you how to deploy this containerized app to Azure Web App for Containers using [Azure Toolkit for Eclipse](https://github.com/Microsoft/azure-tools-for-java).
Below are the major steps in this tutorial.
- [Create Azure Container Registry](#create-acr)
- [Deploy to Azure Web App for Containers using Eclipse Plugin](#deploy)

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

<a name="deploy"></a>
## Deploy to Azure Web App for Containers using Eclipse Plugin
1. Create or import a Maven project in Eclipse. 

1. Right-click on the project and choose "Azure -> Add Docker Support". It will automatically generate a Dockerfile. 

1. Right-click on the project and choose "Azure -> Run on Web App on Linux".  

1. Provide Azure Container Registry and Web App for Containers configuration info, and hit run.

You will see this app successfully running on Azure Web App for Containers.

