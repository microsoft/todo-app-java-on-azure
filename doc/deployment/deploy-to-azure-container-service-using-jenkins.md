# Deploy to Azure Container Service using Jenkins

This document shows how to deploy this todo app java project to Kubernetes cluster using Jenkins.

## Create Azure services

You can create the Azure Services using [Azure CLI 2.0](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli?view=azure-cli-latest).

### Create Azure Container Service

1. login your Azure CLI, and set your subscription id 
    
    ```bash
    az login
    az account set -s <your-subscription-id>
    ```

1. Create a resource group

    ```bash
    az group create -n <your-resource-group-name> -l westeurope
    ```

1. Create Kubernetes cluster

    ```bash
    az acs create --orchestrator-type kubernetes -g <your-resource-group-name> -n <your-kubernetes-cluster-name> --generate-ssh-keys
    ```

1. Connect to the cluster, this command download the Kubernetes configuration to your profile folder. The kubectl will use this configure file to interact with your Kubernetes cluster.

    ```bash
    az acs kubernetes get-credentials -g <your-resource-group-name> -n <your-kubernetes-cluster-name>
    ```

1. Install the kubectl command line

    ```bash
    az acs kubernetes install-cli
    ```

### Create Azure Container Registry

1. login your Azure CLI, and set your subscription id 
   
    ```bash
    az login
    az account set -s <your-subscription-id>
    ```

1. Run below command to create an Azure Container Registry.
After creation, use `login server` as Docker registry URL in the next section.

   ```bash
   az acr create -n <your-registry-name> -g <your-resource-group-name>
   ```

1. Run below command to show your Azure Container Registry credentials.
You will use Docker registry username and password in the next section.

    ```bash
    az acr credential show -n <your-registry-name>
    ```

## Prepare Jenkins server
1. Deploy a Jenkins Master on Azure[https://aka.ms/jenkins-on-azure]

1. Connect to the server with SSH and install the build tools:
   
   ```
   sudo apt-get install git maven docker.io
   ```

1. Install the plugins in Jenkins. Click 'Manage Jenkins' -> 'Manage Plugins' -> 'Available', 
then search and install the following plugins: EnvInject, Azure Kubernetes CD Plugin.

1. Add a Credential in type "SSH Username with private key" with your Kubernetes login
   credential.

1. Add a Credential in type "Username with password" with your account of docker registry.

## Create job
1. Add a new job in type "Pipeline".

1. Enable "Prepare an environment for the run", and put the following environment variables in "Properties Content":
    ```
    ACS_SERVER=[your Azure Container Service server]
    ACS_CRED_ID=[your credential id of ACS login credential]
    ACR_SERVER=[your Azure Container Registry]
    ACR_CRED_ID=[your credential id of ACR account]
    WEB_APP=[the name of the app]
    DOCUMENTDB_URI=[your documentdb uri]
    DOCUMENTDB_KEY=[your documentdb key]
    DOCUMENTDB_DBNAME=[your documentdb databasename]
    ```

1. Choose "Pipeline script from SCM" in "Pipeline" -> "Definition".

1. Fill in the SCM repo url and script path.

1. Create Kubernetes resource yaml file fragments. 

    * Create `src/main/jenkins/jenkins-sample.yml` with yaml content from [this file](../resource/jenkins/jenkins-sample.yml). This file defines a deployment and a service with the docker image you pushed to your Azure Container Registry.


## Run it
1. Verify you can run your project successfully in your local environment. ([Run project on local machine](../../README.md))

1. Run jenkins job.

1. Get the external IP address. This may take a few minutes to wait the deploy success. Before finishing, the `external-ip` field should show `pending`.

    ```bash
    kubectl get svc -w
    ```

1. Open the url you obtained in last step in your browser, you will find the todo app has been deployed to your Kubernetes cluster. 

## Clean up

Delete the Azure resources you just created by running below command:

```bash
az group delete -y --no-wait -n <your-resource-group-name>
```
