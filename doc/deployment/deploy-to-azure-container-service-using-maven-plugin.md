# Deploy to Azure Container Service using Fabric8 Maven Plugin

This document shows how to deploy this todo app java project to Kubernetes cluster using Maven plugin.
It firstly uses Spotify Docker Maven Plugin to build a docker image and push the image to a private Azure Container Registry.
Then, it uses Fabric8 Maven Plugin to generate Kubernetes resource yaml file and apply the yaml file to your cluster.

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

1. Connect to the cluster, this command download the Kubernetes configuration to your profile folder. The Fabric8 Maven Plugin and kubectl will use this configure file to interact with your Kubernetes cluster.

    ```bash
    az acs kubernetes get-credentials -g <your-resource-group-name> -n <your-kubernetes-cluster-name>
    ```

1. Install the kubectl command line

    ```bash
    az acs kubernetes install-cli
    ```

### Create Azure Container Registry

1. Run below command to create an Azure Container Registry.
After creation, use `login server` as Docker registry URL in the next section.

   ```bash
   az acr create -n <your-registry-name> -g <your-resource-group-name> --sku <sku-name>
   ```
       where `<sku-name>` is one of the following: `{Basic,Managed_Basic,Managed_Standard,Managed_Premium}`.

1. Run below command to show your Azure Container Registry credentials.
You will use Docker registry username and password in the next section.

    ```bash
    az acr credential show -n <your-registry-name>
    ```

## Configuration

1. In the Maven settings file `~/.m2/settings.xml`, add a new `<server>` element with your container registry credentials from previous steps.

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

1. In `<properties>` section of the project's `pom.xml`, replace value in `<docker.image.prefix>` element with your docker registry URL.

    ```xml
    <docker.image.prefix>put-your-docker-registry-url</docker.image.prefix>
    ```

1. Create Kubernetes resource yaml file fragments. 

    The Fabric8 Maven Plugin supports putting Kubernetes yaml files in the `src/main/fabric8` folder. And this plugin will aggregate these yaml file into one Kubernetes resource list file, which can be apply to cluster directly.

    * Copy [fabric8/deployment.yml](../resources/fabric8/deployment.yml) to `src/main/fabric8/deployment.yml`. This file defines a deployment with the docker image you pushed to your Azure Container Registry.

    * Copy [fabric8/service.yml](../resources/fabric8/service.yml) to `src/main/fabric8/service.yml`. This file defines a service to expose the deployment to external internet.

    * Copy [fabric8/secrets.yml](../resources/fabric8/secrets.yml) to `src/main/fabric8/secrets.yml`. This file defines a docker-registry secrets, which will be used to pull image from your Azure Container Registry.


## Run it
1. Verify you can run your project successfully in your local environment. ([Run project on local machine](../../README.md))

1. Build the project package

    ```bash
    mvn clean package
    ```

1. Build the docker image and push it to your Azure Container Registry.

    ```bash
    mvn docker:build docker:push
    ```

1. Deploy image to your Kubernetes cluster.

    ```bash
    mvn fabric8:resource fabric8:apply
    ```

    > NOTE: You can also run above commands as a one-liner:  
    > `mvn clean package docker:build docker:push fabric8:resource fabric8:apply`

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
