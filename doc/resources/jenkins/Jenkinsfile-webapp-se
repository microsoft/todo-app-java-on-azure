node {
  stage('init') {
    checkout scm
  }

  stage('build') {
    sh '''
        mvn clean package
        cd target
        cp ../src/main/resources/web.config web.config
        cp todo-app-java-on-azure-1.0-SNAPSHOT.jar app.jar 
        zip todo.zip app.jar web.config
    '''
  }

  stage('deploy') {
    azureWebAppPublish azureCredentialsId: env.AZURE_CRED_ID,
                       resourceGroup: env.RES_GROUP, appName: env.WEB_APP, filePath: "**/todo.zip"

  }
}