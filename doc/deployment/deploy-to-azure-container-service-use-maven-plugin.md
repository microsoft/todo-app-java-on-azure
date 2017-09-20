# Deploy to Azure Container Service use maven plugin

This document shows how to deploy this todo app java project to kubernetes cluster using maven plugin.
It firstly uses spotify maven plugin to builds a docker image and push the image to a private Azure Container Registry.
Then, it uses fabric8 maven plugin to generate kubernetes resource yaml file and apply the yaml file to your cluster.

## Create Azure services

You can create the Azure Container Service using azure-cli ([install azure-cli 2.0]()).

### Create Azure Container Service

1. Create a resource group

    ```bash
    az group create --name <your-resource-group-name> --location westeurope
    ```

1. Create kubernetes cluster

    ```bash
    az acs create --orchestrator-type kubernetes --resource-group <your-resource-group-name> --name <your-kubernetes-cluster-name> --generate-ssh-keys
    ```

1. Connect to the cluster

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

1. Get your Azure Container Registry credential key

    ```bash
    az acr credential show
    ```

## Configuration

1. Save your docker registry key in your maven settings file `~/.m2/settings.xml`

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

1. Add your docker registry url to your `pom.xml`'s `properties` field.

    ```xml
    <docker.image.prefix>put-your-docker-registry-url</docker.image.prefix>
    ```

1. Install the spotify maven plugin to build/push your project docker image, and install the fabric8 maven plugin to build/apply your kubernetes resource yaml file to cluster in the `pom.xml`'s `plugins` field.

    ```xml
    <plugin>
        <groupId>com.spotify</groupId>
        <artifactId>docker-maven-plugin</artifactId>
        <version>0.4.11</version>
        <configuration>
            <imageName>${docker.image.prefix}/${project.artifactId}</imageName>
            <imageTags>
                <imageTag>${project.version}</imageTag>
            </imageTags>
            <baseImage>java</baseImage>
            <entryPoint>["java", "-jar", "/${project.build.finalName}.jar"]</entryPoint>
            <resources>
                <resource>
                    <targetPath>/</targetPath>
                    <directory>${project.build.directory}</directory>
                    <include>${project.build.finalName}.jar</include>
                </resource>
            </resources>
            <registryUrl>https://${docker.image.prefix}</registryUrl>
            <serverId>${docker.image.prefix}</serverId>
        </configuration>
    </plugin>
    <plugin>
        <groupId>io.fabric8</groupId>
        <artifactId>fabric8-maven-plugin</artifactId>
        <version>3.5.30</version>
        <configuration>
            <ignoreServices>false</ignoreServices>
            <registry>${docker.image.prefix}</registry>
        </configuration>
    </plugin>
    ```

1. Create kubernetes resource yaml file fragments. 

    The `fabric8-maven-plugin` supports putting kubernetes yaml files in the `src/main/fabric8` folder. And this plugin will aggregate these yaml file into one kubernetes resource list file, which can be apply to cluster directly.

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

1. Deploy to image to your kubernetes cluster.

    ```bash
    mvn fabric8:resource 
    ```

1. Get the external IP address. This may take a few minutes to wait the deploy success. Before finishing, the `external-ip` field should show `pending`.

    ```bash
    kubectl get svc
    ```

1. Open the url you obtained in last step in your browser, you can find the todo app has been deploy to the kubernetes cluster. 

## Clean up

```bash
az group delete --name <your-resource-group-name> --yes --no-wait
```