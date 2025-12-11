# Exercise: Deploy Application to Kubernetes

## Objective
Deploy a containerized application to Kubernetes, demonstrating pods, deployments, services, ConfigMaps, and basic scaling.

## Requirements

### Kubernetes Resources to Create

1. **Namespace**
   - Isolate application resources

2. **ConfigMap & Secret**
   - Application configuration
   - Database credentials

3. **Deployment**
   - Backend deployment with replicas
   - Frontend deployment
   - Resource limits and requests

4. **Service**
   - ClusterIP for internal communication
   - LoadBalancer/NodePort for external access

5. **Ingress** (optional)
   - Route external traffic
   - TLS termination

### Manifest Files Structure
```
k8s/
├── namespace.yaml
├── configmap.yaml
├── secret.yaml
├── backend/
│   ├── deployment.yaml
│   └── service.yaml
├── frontend/
│   ├── deployment.yaml
│   └── service.yaml
├── database/
│   ├── deployment.yaml
│   ├── service.yaml
│   └── pvc.yaml
└── ingress.yaml
```

### Example Manifests

#### Deployment
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: backend
  namespace: myapp
spec:
  replicas: 3
  selector:
    matchLabels:
      app: backend
  template:
    metadata:
      labels:
        app: backend
    spec:
      containers:
      - name: backend
        image: myapp/backend:1.0
        ports:
        - containerPort: 8080
        env:
        - name: DB_HOST
          valueFrom:
            configMapKeyRef:
              name: app-config
              key: db-host
        resources:
          requests:
            memory: "256Mi"
            cpu: "250m"
          limits:
            memory: "512Mi"
            cpu: "500m"
```

#### Service
```yaml
apiVersion: v1
kind: Service
metadata:
  name: backend-service
spec:
  selector:
    app: backend
  ports:
  - port: 8080
    targetPort: 8080
  type: ClusterIP
```

## Tasks

1. Create namespace
2. Create ConfigMap and Secret
3. Deploy database with PersistentVolumeClaim
4. Deploy backend with 3 replicas
5. Deploy frontend
6. Expose services
7. Test scaling: `kubectl scale deployment backend --replicas=5`
8. View logs and debug pods

## kubectl Commands to Practice
```bash
kubectl apply -f k8s/
kubectl get pods -n myapp
kubectl logs -f deployment/backend -n myapp
kubectl exec -it pod/backend-xxx -- /bin/sh
kubectl scale deployment backend --replicas=5
kubectl rollout status deployment/backend
kubectl rollout undo deployment/backend
```

## Skills Tested
- Kubernetes resource definitions
- Pod lifecycle management
- Service discovery
- Configuration management
- Scaling applications
- Basic troubleshooting
