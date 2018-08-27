# Deploy to Azure Web App for Tomcat using Jenkins

This tutorial shows you how to deploy a Java app to Azure Web App for Tomcat using [Azure App Service Plugin](https://wiki.jenkins.io/display/JENKINS/Azure+App+Service+Plugin).
Below are the major steps in this tutorial.
- [Create Azure Web App](#create-app)
- [Prepare Jenkins server](#prepare)
- [Create job](#create-job)
- [Build and Deploy Java Application to Azure Web App](#deploy)
- [Clean Up Resources](#clean-up)

## <a name="create-app"></a>Create Azure Web App

Create an Azure Web App by [Azure portal](https://github.com/Azure/azure-docs-sdk-java/blob/master/docs-ref-conceptual/spring-framework/deploy-spring-boot-java-web-app-on-azure.md#create-an-azure-web-app-for-use-with-java) or [Azure CLI](https://docs.microsoft.com/en-us/azure/app-service/app-service-cli-samples?toc=%2fazure%2fapp-service%2fcontainers%2ftoc.json).

## <a name="prepare"></a>Prepare Jenkins server

1. Deploy a [Jenkins Master](https://aka.ms/jenkins-on-azure) on Azure

1. Install the plugins in Jenkins. Click 'Manage Jenkins' -> 'Manage Plugins' -> 'Available', 
then search and install the following plugins: Azure App Service Plugin, EnvInject Plugin.

1. Add a Credential in type "Microsoft Azure Service Principal" with your service principal.

## <a name="create-job"></a>Create job

1. Add a new job in type "Pipeline".

1. Enable "Prepare an environment for the run", and put the following environment variables
   in "Properties Content":
    ```
    AZURE_CRED_ID=[your credential id of service principal]
    RES_GROUP=[your resource group of the web app]
    WEB_APP=[the name of the web app]
    DOCUMENTDB_URI=[the URI of the DocumentDB]
    DOCUMENTDB_KEY=[the key of the DocumentDB]
    DOCUMENTDB_DBNAME=[the name of the DocumentDB]
    ```

    > You can save your DocumentDB settings in your Web App Application settings if you would like to manage your application settings independently.

1. This application's default package type is jar. So you have to make some changes to enable it create a deployable war file. Please refer to Spring Boot doc [here](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-create-a-deployable-war-file).

1. Choose "Pipeline script from SCM" in "Pipeline" -> "Definition".

1. Fill in the SCM repo url and script path. ([Script Example](../resources/jenkins/Jenkinsfile-webapp-tomcat))


## <a name="deploy"></a>Build and Deploy Java SE application to Azure Web App

1. Verify you can run your project successfully in your local environment. ([Run project on local machine](../../README.md))

1. Run jenkins job.

1. Navigate to the website from your favorite browser.
You will see this app successfully running on Azure Web App.


## <a name="clean-up"></a>Clean Up Resources

Delete the Azure resources you just created by running below command:

```bash
az group delete -y --no-wait -n <your-resource-group-name>
```
