node {
  stage('init') {
    checkout scm
  }

  stage('build') {
    sh '''
        mvn clean package
        mv target/*.war ROOT.war
    '''
  }

  stage('deploy') {
    azureWebAppPublish azureCredentialsId: env.AZURE_CRED_ID,
                       resourceGroup: env.RES_GROUP, appName: env.WEB_APP, filePath: "ROOT.war",
                       targetDirectory: "webapps"
  }
}