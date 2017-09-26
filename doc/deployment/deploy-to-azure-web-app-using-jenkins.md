# Deploy to Azure Web App for Containers using Maven Plugin

This tutorial shows you how to deploy this containerized app to Azure Web App for Containers using [Azure App Service Plugin](https://wiki.jenkins.io/display/JENKINS/Azure+App+Service+Plugin).
Below are the major steps in this tutorial.
- [Create Azure Container Registry](#create-acr)
- [Setup and Customize Configuration](#config)
- [Build and Deploy Docker Container Image to Azure Web App for Containers](#deploy)
- [Clean Up Resources](#clean-up)

<a name="create-acr"></a>
## Create Azure Container Registry

1. Run below command to create an Azure Container Registry.
After creation, use `login server` as Container Registry URL in the next section.

   ```bash
   az acr create -n <your-registry-name> -g <your-resource-group-name>
   ```

1. Run below command to show your Azure Container Registry credentials.
You will use Docker registry username and password in the next section.

    ```bash
    az acr credential show -n <your-registry-name>
    ```

<a name="config"></a>
## Setup and Customize Configuration

1. Install "EnvInject" plugin.

1. Add a Credential in type "Microsoft Azure Service Principal" with your service principal.

1. Add a Credential in type "Username with password	" with your account of docker registry.

1. Add a pipeline job.

1. Enable "Prepare an environment for the run", and put the following environment variables
   in "Properties Content":
    ```
    AZURE_CRED_ID=[your credential id of service principal]
    RES_GROUP=[your resource group of the web app]
    WEB_APP=[the name of the web app]
    ACR_SERVER=[your address of azure container registry]
    ACR_CRED_ID=[your credential id of ACR account]
    ```

1. Choose "Pipeline script from SCM" in "Pipeline" -> "Definition".

1. Fill in the SCM repo and script path.

<a name="deploy"></a>
## Build and Deploy Docker Container Image to Azure Web App for Containers
1. Verify you can run your project successfully in your local environment. ([Run project on local machine](../../README.md))

1. Run jenkins job.

1. Navigate to the website from your favorite browser.
You will see this app successfully running on Azure Web App for Containers.

<a name="clean-up"></a>
## Clean Up Resources

Delete the Azure resources you just created by running below command:

```bash
az group delete -y --no-wait -n <your-resource-group-name>
```
