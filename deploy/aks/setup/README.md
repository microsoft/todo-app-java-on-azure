# Setup AKS

## Command

```sh
./setup.sh
```

## Prerequisites

* Login Azure CLI

   ```sh
   az login
   ```

* Select the demos subscription

   ```sh
   az account set --subscription "<subscription-id>"
   ```

## Actions Performed

* Create resource group `<your-resource-group-name>` if not exists
* Create AKS `<your-kubernetes-cluster-name>` in resource group `<your-resource-group-name>` if not exists
* Apply the Kubernetes resource configurations in this directory

