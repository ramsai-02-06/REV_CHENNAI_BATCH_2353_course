# Kubernetes kubectl Cheat Sheet

## Setup & Configuration

```bash
# Install kubectl
# macOS
brew install kubectl

# Ubuntu
sudo apt-get update
sudo apt-get install -y kubectl

# Check version
kubectl version --client

# Configure kubectl (uses ~/.kube/config)
kubectl config view

# Get cluster info
kubectl cluster-info

# Check current context
kubectl config current-context

# List all contexts
kubectl config get-contexts

# Switch context
kubectl config use-context context-name

# Set namespace for current context
kubectl config set-context --current --namespace=my-namespace
```

## Cluster Information

```bash
# Cluster info
kubectl cluster-info
kubectl cluster-info dump

# Get nodes
kubectl get nodes
kubectl get nodes -o wide

# Describe node
kubectl describe node node-name

# Get node resource usage
kubectl top node
kubectl top node node-name

# Get cluster events
kubectl get events
kubectl get events --sort-by=.metadata.creationTimestamp
```

## Pods

```bash
# List pods
kubectl get pods
kubectl get pods -o wide
kubectl get pods --all-namespaces
kubectl get pods -n namespace-name

# Create pod from YAML
kubectl apply -f pod.yaml
kubectl create -f pod.yaml

# Get pod details
kubectl describe pod pod-name
kubectl describe pod pod-name -n namespace-name

# Get pod logs
kubectl logs pod-name
kubectl logs pod-name -c container-name  # Specific container
kubectl logs -f pod-name                 # Follow logs
kubectl logs --tail=100 pod-name         # Last 100 lines
kubectl logs --since=1h pod-name         # Last hour

# Execute command in pod
kubectl exec pod-name -- ls /app
kubectl exec -it pod-name -- /bin/bash
kubectl exec -it pod-name -- sh

# Port forward
kubectl port-forward pod-name 8080:80

# Delete pod
kubectl delete pod pod-name
kubectl delete pod pod-name --grace-period=0 --force

# Get pod YAML
kubectl get pod pod-name -o yaml
kubectl get pod pod-name -o json

# Watch pods
kubectl get pods --watch
kubectl get pods -w
```

## Deployments

```bash
# List deployments
kubectl get deployments
kubectl get deploy

# Create deployment
kubectl create deployment nginx --image=nginx
kubectl apply -f deployment.yaml

# Describe deployment
kubectl describe deployment deployment-name

# Scale deployment
kubectl scale deployment deployment-name --replicas=3

# Autoscale deployment
kubectl autoscale deployment deployment-name --min=2 --max=10 --cpu-percent=80

# Update deployment image
kubectl set image deployment/deployment-name container-name=nginx:1.20

# Rollout status
kubectl rollout status deployment/deployment-name

# Rollout history
kubectl rollout history deployment/deployment-name

# Rollback deployment
kubectl rollout undo deployment/deployment-name
kubectl rollout undo deployment/deployment-name --to-revision=2

# Pause/Resume rollout
kubectl rollout pause deployment/deployment-name
kubectl rollout resume deployment/deployment-name

# Delete deployment
kubectl delete deployment deployment-name

# Edit deployment
kubectl edit deployment deployment-name
```

## Services

```bash
# List services
kubectl get services
kubectl get svc

# Create service
kubectl expose deployment nginx --port=80 --target-port=80 --type=LoadBalancer
kubectl apply -f service.yaml

# Describe service
kubectl describe service service-name

# Get service endpoints
kubectl get endpoints service-name

# Delete service
kubectl delete service service-name

# Get service URL (Minikube)
minikube service service-name --url
```

## Namespaces

```bash
# List namespaces
kubectl get namespaces
kubectl get ns

# Create namespace
kubectl create namespace my-namespace

# Delete namespace
kubectl delete namespace my-namespace

# Get resources in namespace
kubectl get all -n my-namespace

# Set default namespace
kubectl config set-context --current --namespace=my-namespace
```

## ConfigMaps

```bash
# List ConfigMaps
kubectl get configmaps
kubectl get cm

# Create ConfigMap from file
kubectl create configmap my-config --from-file=config.properties

# Create ConfigMap from literal
kubectl create configmap my-config --from-literal=key1=value1 --from-literal=key2=value2

# Create from YAML
kubectl apply -f configmap.yaml

# Describe ConfigMap
kubectl describe configmap my-config

# Get ConfigMap YAML
kubectl get configmap my-config -o yaml

# Delete ConfigMap
kubectl delete configmap my-config
```

## Secrets

```bash
# List secrets
kubectl get secrets

# Create secret from literal
kubectl create secret generic my-secret --from-literal=username=admin --from-literal=password=pass123

# Create secret from file
kubectl create secret generic my-secret --from-file=ssh-key=~/.ssh/id_rsa

# Create docker registry secret
kubectl create secret docker-registry my-registry \
  --docker-server=registry.example.com \
  --docker-username=user \
  --docker-password=pass \
  --docker-email=user@example.com

# Create from YAML
kubectl apply -f secret.yaml

# Describe secret
kubectl describe secret my-secret

# Get secret (base64 encoded)
kubectl get secret my-secret -o yaml

# Decode secret
kubectl get secret my-secret -o jsonpath='{.data.password}' | base64 --decode

# Delete secret
kubectl delete secret my-secret
```

## Persistent Volumes

```bash
# List Persistent Volumes
kubectl get pv

# List Persistent Volume Claims
kubectl get pvc

# Create PVC
kubectl apply -f pvc.yaml

# Describe PVC
kubectl describe pvc pvc-name

# Delete PVC
kubectl delete pvc pvc-name
```

## Resource Management

```bash
# Get all resources
kubectl get all
kubectl get all -n namespace-name

# Get multiple resource types
kubectl get pods,services,deployments

# Delete all resources in namespace
kubectl delete all --all -n namespace-name

# Apply directory of YAML files
kubectl apply -f ./kubernetes/

# Delete from file
kubectl delete -f deployment.yaml

# Get resource usage
kubectl top pods
kubectl top nodes
```

## Labels and Selectors

```bash
# Show labels
kubectl get pods --show-labels
kubectl get nodes --show-labels

# Filter by label
kubectl get pods -l app=nginx
kubectl get pods -l 'env in (prod,staging)'
kubectl get pods -l app=nginx,env=prod

# Add label
kubectl label pod pod-name env=prod
kubectl label pod pod-name tier=frontend

# Remove label
kubectl label pod pod-name env-

# Update label
kubectl label pod pod-name env=staging --overwrite
```

## Debugging

```bash
# Describe resource
kubectl describe pod pod-name
kubectl describe service service-name

# Get logs
kubectl logs pod-name
kubectl logs -f pod-name
kubectl logs pod-name --previous  # Previous container logs

# Execute commands
kubectl exec -it pod-name -- /bin/bash
kubectl exec pod-name -- env
kubectl exec pod-name -- ps aux

# Debug with temporary pod
kubectl run debug --image=busybox -it --rm -- sh

# Port forwarding for debugging
kubectl port-forward pod-name 8080:80

# Copy files
kubectl cp pod-name:/app/logs/app.log ./app.log
kubectl cp ./config.yaml pod-name:/app/config.yaml

# Get events
kubectl get events --sort-by=.metadata.creationTimestamp
kubectl get events --field-selector involvedObject.name=pod-name

# Troubleshoot network
kubectl run tmp-shell --rm -i --tty --image nicolaka/netshoot -- /bin/bash
```

## YAML Examples

### Pod

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: nginx-pod
  labels:
    app: nginx
spec:
  containers:
  - name: nginx
    image: nginx:1.20
    ports:
    - containerPort: 80
```

### Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-deployment
spec:
  replicas: 3
  selector:
    matchLabels:
      app: nginx
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx:1.20
        ports:
        - containerPort: 80
        resources:
          requests:
            memory: "64Mi"
            cpu: "250m"
          limits:
            memory: "128Mi"
            cpu: "500m"
```

### Service

```yaml
apiVersion: v1
kind: Service
metadata:
  name: nginx-service
spec:
  type: LoadBalancer
  selector:
    app: nginx
  ports:
  - protocol: TCP
    port: 80
    targetPort: 80
```

### ConfigMap

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: app-config
data:
  database.host: "mysql"
  database.port: "3306"
  app.env: "production"
```

### Secret

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: app-secret
type: Opaque
data:
  username: YWRtaW4=  # base64 encoded
  password: cGFzczEyMw==
```

### PersistentVolumeClaim

```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: mysql-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 10Gi
```

## Context and Kubeconfig

```bash
# View config
kubectl config view

# Get contexts
kubectl config get-contexts

# Set current context
kubectl config use-context my-context

# Set namespace
kubectl config set-context --current --namespace=dev

# Create new context
kubectl config set-context my-context \
  --cluster=my-cluster \
  --user=my-user \
  --namespace=default

# Delete context
kubectl config delete-context my-context
```

## Common Patterns

### Rolling Update

```bash
# Update image
kubectl set image deployment/nginx nginx=nginx:1.21

# Check rollout status
kubectl rollout status deployment/nginx

# Rollback if needed
kubectl rollout undo deployment/nginx
```

### Scaling

```bash
# Manual scaling
kubectl scale deployment nginx --replicas=5

# Autoscaling
kubectl autoscale deployment nginx --min=2 --max=10 --cpu-percent=80

# Check HPA
kubectl get hpa
```

### Resource Quotas

```yaml
apiVersion: v1
kind: ResourceQuota
metadata:
  name: compute-quota
  namespace: dev
spec:
  hard:
    requests.cpu: "10"
    requests.memory: 20Gi
    limits.cpu: "20"
    limits.memory: 40Gi
    pods: "50"
```

## Useful Aliases

```bash
# Add to ~/.bashrc or ~/.zshrc
alias k='kubectl'
alias kgp='kubectl get pods'
alias kgs='kubectl get services'
alias kgd='kubectl get deployments'
alias kdp='kubectl describe pod'
alias kl='kubectl logs'
alias kex='kubectl exec -it'
alias kaf='kubectl apply -f'
alias kdf='kubectl delete -f'
```

## Quick Reference

```bash
# Basic operations
kubectl get <resource>
kubectl describe <resource> <name>
kubectl create -f <file>
kubectl apply -f <file>
kubectl delete <resource> <name>
kubectl edit <resource> <name>

# Common resources
pods (po)
services (svc)
deployments (deploy)
replicasets (rs)
namespaces (ns)
configmaps (cm)
secrets
persistentvolumes (pv)
persistentvolumeclaims (pvc)
nodes (no)

# Output formats
-o wide           # More details
-o yaml           # YAML format
-o json           # JSON format
-o name           # Just names
-o jsonpath='{}'  # Custom output
```

## Best Practices

1. **Use namespaces**: Organize resources logically
2. **Set resource limits**: Prevent resource exhaustion
3. **Use labels**: Organize and select resources
4. **Health checks**: Implement liveness and readiness probes
5. **ConfigMaps/Secrets**: Externalize configuration
6. **Rolling updates**: Zero-downtime deployments
7. **RBAC**: Implement proper access control
8. **Monitoring**: Use logging and metrics
9. **Backup**: Regular backups of important data
10. **Documentation**: Document your configurations

---

**Remember:**
- Always specify resource requests and limits
- Use readiness and liveness probes
- Keep images small and secure
- Use specific image tags, not 'latest'
- Implement proper logging
- Regular updates and security patches
