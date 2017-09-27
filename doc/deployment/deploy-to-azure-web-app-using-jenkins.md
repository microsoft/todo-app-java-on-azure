# Deploy to Azure Web App for Containers using Jenkins

This tutorial shows you how to deploy this containerized app to Azure Web App for Containers using [Azure App Service Plugin](https://wiki.jenkins.io/display/JENKINS/Azure+App+Service+Plugin).
Below are the major steps in this tutorial.
- [Create Azure Container Registry](#create-acr)
- [Prepare Jenkins server](#prepare)
- [Create job](#create-job)
- [Build and Deploy Docker Container Image to Azure Web App for Containers](#deploy)
- [Clean Up Resources](#clean-up)

## <a name="create-acr"></a>Create Azure Container Registry

1. login your Azure CLI, and set your subscription id 
   
    ```bash
    az login
    az account set -s <your-subscription-id>
    ```

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

## <a name="prepare"></a>Prepare Jenkins server

1. Deploy a [Jenkins Master](https://aka.ms/jenkins-on-azure) on Azure

1. Connect to the server with SSH and install the build tools:
   ```
   sudo apt-get install git maven docker.io
   ```

1. Install the plugins in Jenkins. Click 'Manage Jenkins' -> 'Manage Plugins' -> 'Available', 
then search and install the following plugins: EnvInject, Azure App Service Plugin.

1. Add a Credential in type "Microsoft Azure Service Principal" with your service principal.

1. Add a Credential in type "Username with password" with your account of docker registry.

## <a name="create-job"></a>Create job

1. Add a new job in type "Pipeline".

1. Enable "Prepare an environment for the run", and put the following environment variables
   in "Properties Content":
    ```
    AZURE_CRED_ID=[your credential id of service principal]
    RES_GROUP=[your resource group of the web app]
    WEB_APP=[the name of the web app]
    ACR_SERVER=[your address of azure container registry]
    ACR_CRED_ID=[your credential id of ACR account]
    DOCUMENTDB_URI=[your documentdb uri]
    DOCUMENTDB_KEY=[your documentdb key]
    DOCUMENTDB_DBNAME=[your documentdb databasename]
    ```

1. Choose "Pipeline script from SCM" in "Pipeline" -> "Definition".

1. Fill in the SCM repo url and script path.


## <a name="deploy"></a>Build and Deploy Docker Container Image to Azure Web App for Containers

1. Verify you can run your project successfully in your local environment. ([Run project on local machine](../../README.md))

1. Run jenkins job.

1. Navigate to the website from your favorite browser.
You will see this app successfully running on Azure Web App for Containers.


## <a name="clean-up"></a>Clean Up Resources

Delete the Azure resources you just created by running below command:

```bash
az group delete -y --no-wait -n <your-resource-group-name>
```
