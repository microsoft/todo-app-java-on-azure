# Build Docker image from git repo in Azure Container Registry then deploy to Azure Kubernetes Service using Jenkins

This document shows how to deploy this todo app java project to Kubernetes cluster using Jenkins. Instead of installing Docker on the build agent, you can use **Azure ACR Plugin** to build your Docker image in Azure Container Registry with your Github repo url.

On the Jenkins machine, it uses **Azure ACR PLugin** to queue an Azure Container Registry Quick build to build a todo-app-java-on-azure docker image, then apply the docker image to an Azure Kubernetes Service cluster with **Azure Kubernetes CD Plugin**. 

> This deployment instruction will include Maven package in the Dockerfile. If you want to do the Maven package on your Jenkins Server instead during the docker build, please go to [Build Docker image from local directory in Azure Container Registry then deploy to Azure Kubernetes Service using Jenkins](./deploy-to-aks-with-acr-build-local-using-jenkins.md).

## Run application on local machine
Verify you can run your project successfully in your local environment. ([Run project on local machine](../../README.md))

## Create Azure services

You can create the Azure Services using [Azure CLI 2.0](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli?view=azure-cli-latest).

1. login your Azure CLI, and set your subscription id 
    
    ```bash
    az login
    az account set -s <your-subscription-id>
    ```

1. Create a resource group

    ```bash
    az group create -n <your-resource-group-name> -l eastus
    ```

### Create Azure Service Principal

1. Create a service principal  and configure its access to all Azure resources under this subscription. Note all the information as service principal.

   ```bash
   az ad sp create-for-rbac
   ```
    
### Create Azure Kubernetes Service

1. Create Kubernetes cluster

    ```bash
    az aks create -g <your-resource-group-name> -n <your-kubernetes-cluster-name> --generate-ssh-keys
    ```

1. Install `kubectl` on your local machine

   ```bash
   az aks install-cli
   ```

1. Get access credentials for a managed Kubernetes cluster and save to local machine.

    ```bash
    az aks get-credentials -g <your-resource-group-name> -n <your-kubernetes-cluster-name>
    ```

1. Get access credentials for a managed Kubernetes cluster. Note the yaml output as `kubeconfig`.

    ```bash
    az aks get-credentials -g <your-resource-group-name> -n <your-kubernetes-cluster-name> -f -
    ```

### Create Azure Container Registry
1. Run below command to create an Azure Container Registry.
After creation, use `login server` as Docker registry URL in the next section.

   ```bash
   az acr create -n <your-registry-name> -g <your-resource-group-name> --sku <sku-name> --admin-enabled true
   ```

1. Run below command to show your Azure Container Registry credentials.
You will use Docker registry username and password in the next section.

    ```bash
    az acr credential show -n <your-registry-name>
    ```

## Prepare Jenkins server
1. Deploy a [Jenkins Master on Azure](https://aka.ms/jenkins-on-azure)

1. Install the plugins in Jenkins. 

   1. Click 'Manage Jenkins' -> 'Manage Plugins' -> 'Available', 
      then search and install the following plugins: [EnvInject](https://wiki.jenkins.io/display/JENKINS/EnvInject+Plugin), [Azure Kubernetes CD Plugin](https://wiki.jenkins.io/display/JENKINS/Azure+Container+Service+Plugin).
   1. Download Azure-acr-plugin latest preview release `hpi` file from [GitHub](https://github.com/Azure/azure-acr-plugin/releases).
      Go to Jenkins page, click `Manage Jenkins` -> `Manage Plugins` -> `Advanced` -> `Upload Plugin`,
      upload the azure-acr-plugin hpi file.

1. Add a Credential in type "Microsoft Azure Service Principal" with the service principal you created. Note the ID as `AZURE_CRED`.

1. Add a Credential in type "Username with password" with your account of docker registry. Note the ID as `ACR_CRED`.

1. Add a Credential in type "Kubernetes configuration (kubeconfig)" -> "Enter directly", with the kubeconfig you noted when creating AKS.


## Create job
1. Add a new job in type "Pipeline".

1. Enable "Prepare an environment for the run", and put the following environment variables in "Properties Content":
    ```
    AZURE_CRED_ID=[your Azure Credential ID]
    RES_GROUP=[your resource group]
    ACR_NAME=[your ACR name]
    ACR_USERNAME=[your registry username]
    ACR_REGISTRY=[your ACR registry url, without http schema]
    ACR_CREDENTIAL_ID=[your credential id of ACR account]
    ACR_SECRET=[secret name you will created in AKS to store ACR credential]
    AKS_NAME=[your AKS name]
    IMAGE_NAME=[image name you will push to ACR, without registry prefix]
    DOCUMENTDB_URI=[your documentdb uri]
    DOCUMENTDB_KEY=[your documentdb key]
    DOCUMENTDB_DBNAME=[your documentdb databasename]
    ```

1. Choose "Pipeline script" in "Pipeline" -> "Definition".

1. Fill the Script part with content in [Jenkinsfile-acr-with-git](../resources/jenkins/Jenkinsfile-acr-with-git)

   > In the `Jenkinsfile-acr-with-git`, it defines the pipeline step logic:
   > 1. stage('build') - Send the GitHub URL to ACR to queue a build.
   > 1. stage('deploy') - Apply a deployment to AKS with the new built docker image. Then expose the deployment to external.

## Run it
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
