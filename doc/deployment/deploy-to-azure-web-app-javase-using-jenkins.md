# Deploy to Azure Web App for Java SE applictions using Jenkins

This tutorial shows you how to deploy a Java SE app to Azure Web App using [Azure App Service Plugin](https://wiki.jenkins.io/display/JENKINS/Azure+App+Service+Plugin).
Below are the major steps in this tutorial.
- [Create Azure Web App](#create-app)
- [Prepare Jenkins server](#prepare)
- [Create job](#create-job)
- [Build and Deploy Java SE Application to Azure Web App](#deploy)
- [Clean Up Resources](#clean-up)

## <a name="create-app"></a>Create Azure Web App

Create an Azure Web App by [Azure portal](https://github.com/Azure/azure-docs-sdk-java/blob/master/docs-ref-conceptual/spring-framework/deploy-spring-boot-java-web-app-on-azure.md#create-an-azure-web-app-for-use-with-java) or [Azure CLI](https://docs.microsoft.com/en-us/azure/app-service/app-service-cli-samples?toc=%2fazure%2fapp-service%2fcontainers%2ftoc.json)

> If you use Web App on Linux with jre runtime stack, you have to build your app with port 80 or set the web app setting with `PORT=8080`

## <a name="prepare"></a>Prepare Jenkins server

1. Deploy a [Jenkins Master](https://aka.ms/jenkins-on-azure) on Azure

1. Install the plugins in Jenkins. Click 'Manage Jenkins' -> 'Manage Plugins' -> 'Available', 
then search and install the following plugins: Azure App Service Plugin.

1. Add a Credential in type "Microsoft Azure Service Principal" with your service principal.

## <a name="create-job"></a>Create job

1. Add a new job in type "Pipeline".

1. Enable "Prepare an environment for the run", and put the following environment variables
   in "Properties Content":
    ```
    AZURE_CRED_ID=[your credential id of service principal]
    RES_GROUP=[your resource group of the web app]
    WEB_APP=[the name of the web app]
    ```

1. Copy [jenkins/web.config](../resources/jenkins/web.config) to `src/main/resources/web.config` and replace the `${JAR_FILE_NAME}` with your app's jar file name.
   
1. Choose "Pipeline script from SCM" in "Pipeline" -> "Definition".

1. Fill in the SCM repo url and script path. ([Script Example](../resources/jenkins/Jenkinsfile-webapp-se))

1. Archive your jar file and web.config in zip format manually or in command like the example script above.


## <a name="deploy"></a>Build and Deploy Java SE application to Azure Web App

1. Verify you can run your project successfully in your local environment. ([Run project on local machine](../../README.md))

1. Run jenkins job.

1. Navigate to the website from your favorite browser.
You will see this app successfully running on Azure Web App for Containers.


## <a name="clean-up"></a>Clean Up Resources

Delete the Azure resources you just created by running below command:

```bash
az group delete -y --no-wait -n <your-resource-group-name>
```
