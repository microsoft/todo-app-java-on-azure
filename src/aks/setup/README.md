# Setup AKS

## Command

```sh
./setup.sh
```

## Prerequisites

* Login Azure CLI
* Select the demos subscription

   ```sh
   az account set --subscription "<subscription-id>"
   ```

## Actions Performed

* Create resource group `jenkins-aks-demo` if not exists
* Create AKS `aks` in resource group `jenkins-aks-demo` if not exists
* Apply the Kubernetes resource configurations in this directory

## Post Actions

Run the job [Deploy-TODO-App-To-AKS-Blue-Green](http://jenkins-demo-1.westus2.cloudapp.azure.com/job/Deploy-TODO-App-To-AKS-Blue-Green/)
at least once to flush the placeholder deployment which uses public Tomcat container image.
