# Deploy to Azure Container Service using Fabric8 Maven Plugin

This document shows how to deploy this todo app java project to Kubernetes cluster using Maven plugin.
It firstly uses Spotify Docker Maven Plugin to build a docker image and push the image to a private Azure Container Registry.
Then, it uses Fabric8 Maven Plugin to generate Kubernetes resource yaml file and apply the yaml file to your cluster.

## Create Azure services

You can create the Azure Services using azure-cli ([install Azure CLI 2.0](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli?view=azure-cli-latest)).

### Create Azure Container Service

1. Create a resource group

    ```bash
    az group create --name <your-resource-group-name> --location westeurope
    ```

1. Create Kubernetes cluster

    ```bash
    az acs create --orchestrator-type kubernetes --resource-group <your-resource-group-name> --name <your-kubernetes-cluster-name> --generate-ssh-keys
    ```

1. Connect to the cluster, this command download the Kubernetes configuration to your profile folder. The Fabric8 Maven Plugin and kubectl will use this configure file to interact with your Kubernetes cluster.

    ```bash
    az acs kubernetes get-credentials --resource-group=<your-resource-group-name> --name=<your-kubernetes-cluster-name>
    ```

1. Install the kubectl command line

    ```bash
    az acs kubernetes install-cli
    ```

### Create Azure Container Registry

1. Create your Azure Container Registry service, after created, note your `login server` as your docker registry url

   ```bash
   az acr create -n <your-registry-name> -g <your-resource-group-name>
   ```

1. Get your Azure Container Registry credential key, note your docker registry username and password.

    ```bash
    az acr credential show
    ```

## Configuration

1. Save your docker registry key in your Maven settings file `~/.m2/settings.xml`

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

1. Modify the `pom.xml`'s `properties` field with your docker registry url.

    ```xml
    <docker.image.prefix>put-your-docker-registry-url</docker.image.prefix>
    ```

1. Create Kubernetes resource yaml file fragments. 

    The Fabric8 Maven Plugin supports putting Kubernetes yaml files in the `src/main/fabric8` folder. And this plugin will aggregate these yaml file into one Kubernetes resource list file, which can be apply to cluster directly.

    * Create `src/main/fabric8/deployment.yml` with yaml content from [this file](../resource/fabric8/deployment.yml). This file defines a deployment with the docker image you pushed to your Azure Container Registry.

    * Create `src/main/fabric8/service.yml` with yaml content from [this file](../resource/fabric8/service.yml). This file defines a service to expose the deployment to external internet.

    * Create `src/main/fabric8/secrets.yml` with yaml content from [this file](../resource/fabric8/secrets.yml). This file defines a docker-registry secrets, which will be used to pull image from your Azure Container Registry.


## Run it
1. Verify you can run your project successfully in your local envrionment. ([Run project on local machine](../../README.md))

1. Build the project package

    ```bash
    mvn package
    ```

1. Build the docker image and push it to your Azure Container Registry.

    ```bash
    mvn docker:build docker:push
    ```

1. Deploy image to your Kubernetes cluster.

    ```bash
    mvn fabric8:resource fabric8:apply
    ```

    > You can also combine these Maven command into one line:  
    > `mvn package docker:build docker:push fabric8:resource fabric8:apply`

1. Get the external IP address. This may take a few minutes to wait the deploy success. Before finishing, the `external-ip` field should show `pending`.

    ```bash
    kubectl get svc -w
    ```

1. Open the url you obtained in last step in your browser, you can find the todo app has been deployed to the kubernetes cluster. 

## Clean up

Delete the Azure resources you created by running:

```bash
az group delete --name <your-resource-group-name> --yes --no-wait
```