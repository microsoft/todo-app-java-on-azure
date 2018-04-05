# Deploy to Cloud Foundry running on Azure

This tutorial shows you how to deploy this app to Cloud Foundry running on Azure, and bind it to a Cosmos
instance that's provisioned by a service broker. 

Below are the major steps in this tutorial.
- [Install 'cf' CLI](#install-cf-cli)
- [Deploy and Configure Cloud Foundry on Azure](#deploy-cf)
- [Create a Cosmos Service Instance](#deploy-cosmos)
- [Build and Deploy App](#deploy-app)
- [Clean Up Resources](#clean-up)

<a name="install-cf-cli"></a>
## Install 'cf' CLI

Developers principally interact with Cloud Foundry on the command line, via the 'cf' CLI.  You will need this tool
in order to complete the steps below.  The CLI is available for Windows, MacOS, and linux - just follow the
[instructions](https://docs.cloudfoundry.org/cf-cli/install-go-cli.html) appropriate to your platform.

<a name="deploy-cf"></a>
## Deploy and Configure Cloud Foundry on Azure

Enterprise deployments of Cloud Foundry typically comprise many VMs and are shared across multiple users, and
setting one up from scratch is outside the scope of this tutorial.  If you have a deployment available to you
already, you will probably prefer to use it instead of creating a new one.  However, if you don't have a 
deployment already or want to start from scratch, you can find choices, instructions, and more information 
[here](https://docs.microsoft.com/en-us/azure/cloudfoundry/).


To proceed, you will need to know the FQDN of your Cloud Foundry API.  For example, it may look like this:
```
api.system.<IP address>.cf.pcfazure.com 
```

1. To deploy the app to Cloud Foundry, you will need a user account, an org and space, and the SpaceDeveloper
role for the space assigned to the user.  If you already have these, [skip to the next step](#deploy-broker).
Otherwise, login as an admin and satisfy these prerequisites as follows:
    
   ```bash
   cf login -a <api-fqdn> --skip-ssl-validation
   cf create-user <user> <password>
   cf create-org <org>
   cf create-space <space> -o <org>
   cf set-space-role <user> <org> <space> SpaceDeveloper
   ```

<a name="deploy-broker"></a>
2. To create a Cosmos service instance for the app to bind to, you will need an Azure service broker.  There
are two to choose from: the [Meta Azure Service Broker (MASB)](https://github.com/Azure/meta-azure-service-broker)
and the [Open Service Broker for Azure (OSBA)](https://github.com/Azure/open-service-broker-azure).  MASB is
fully GA and being actively maintained, but new development will be for OSBA, which is expected to replace MASB
over time.  MASB is installed by default with the PCF offering on the Azure Marketplace, but if you prefer OSBA,
or are using a deployment that does not have an Azure service broker installed, you will need to install and
configure it.  Doing so is outside the scope of this tutorial, but full instructions are available at the links
above.

<a name="deploy-cosmos"></a>
## Create a Cosmos Service Instance
If you last logged in as an admin, for example to execute the steps above, log in to Cloud Foundry with the
credentials that have the SpaceDeveloper role for the space you're going to use.

   ```bash
   cf login -a <api-fqdn> --skip-ssl-validation
   ```

To create a Cosmos service instance with MASB:
   ```bash
   cf create-service azure-cosmosdb standard todo-db
   ```

Alternately, to create a Cosmos service instance with OSBA:
   ```bash
   cf create-service azure-cosmosdb-sql-account account todo-db
   ```

<a name="deplouy-app"></a>
## Build and Deploy App

1. In the manifest.yml file, set the Spring profile according to which service broker you are using.

For MASB:
   ```yml
   env:
     SPRING_PROFILES_ACTIVE: masb
   ```

For OSBA:
   ```yml
   env:
     SPRING_PROFILES_ACTIVE: osba
   ```   

2. Build project and docker container image, and push the image to Cloud Foundry.

    ```bash
    mvn clean package
    cf push todo-app -p target/todo-app-java-on-azure-1.0-SNAPSHOT.jar
    ```

3. Navigate to the website from your favorite browser.
You will see this app successfully running in Cloud Foundry on Azure.

<a name="clean-up"></a>
## Clean Up Resources

Unbind your app, delete it, and delete the Cosmos service instance:
   ```bash
   cf unbind-service todo-app todo-db
   cf delete todo-app
   cf delete-service todo-db
   ```

If you deployed Cloud Foundry yourself, and would like to delete it:
   ```bash
   az group delete -y --no-wait -n <your-resource-group-name>
   ```
