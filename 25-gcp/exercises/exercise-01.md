# Exercise: Deploy Application to Google Cloud Platform

## Objective
Deploy a full-stack application to GCP using Cloud Run, Cloud SQL, and Cloud Storage.

## Requirements

### GCP Services to Use

1. **Cloud Run**
   - Deploy containerized backend and frontend
   - Serverless, auto-scaling

2. **Cloud SQL**
   - Managed MySQL instance
   - Connect from Cloud Run

3. **Cloud Storage**
   - Store static assets
   - File uploads

4. **Secret Manager**
   - Store database credentials
   - API keys

5. **Cloud Build**
   - CI/CD pipeline
   - Build and deploy containers

### Architecture
```
[Users] → [Cloud Load Balancer]
              ↓
        [Cloud Run - Frontend]
              ↓
        [Cloud Run - Backend]
              ↓
        [Cloud SQL - MySQL]
              ↓
        [Cloud Storage]
```

### Deployment Steps

1. **Setup GCP Project**
   ```bash
   gcloud projects create my-project-id
   gcloud config set project my-project-id
   gcloud services enable run.googleapis.com sqladmin.googleapis.com
   ```

2. **Create Cloud SQL Instance**
   ```bash
   gcloud sql instances create myinstance \
     --database-version=MYSQL_8_0 \
     --tier=db-f1-micro \
     --region=us-central1
   ```

3. **Deploy to Cloud Run**
   ```bash
   gcloud run deploy backend \
     --image gcr.io/my-project/backend:latest \
     --platform managed \
     --region us-central1 \
     --add-cloudsql-instances my-project:us-central1:myinstance \
     --set-env-vars INSTANCE_CONNECTION_NAME=my-project:us-central1:myinstance
   ```

4. **Configure Secret Manager**
   ```bash
   echo -n "password123" | gcloud secrets create db-password --data-file=-
   ```

### cloudbuild.yaml
```yaml
steps:
  # Build backend
  - name: 'gcr.io/cloud-builders/docker'
    args: ['build', '-t', 'gcr.io/$PROJECT_ID/backend:$COMMIT_SHA', './backend']

  # Push to Container Registry
  - name: 'gcr.io/cloud-builders/docker'
    args: ['push', 'gcr.io/$PROJECT_ID/backend:$COMMIT_SHA']

  # Deploy to Cloud Run
  - name: 'gcr.io/google.com/cloudsdktool/cloud-sdk'
    entrypoint: gcloud
    args:
      - 'run'
      - 'deploy'
      - 'backend'
      - '--image=gcr.io/$PROJECT_ID/backend:$COMMIT_SHA'
      - '--region=us-central1'
      - '--platform=managed'
```

## Tasks

1. Create GCP project and enable APIs
2. Create Cloud SQL instance
3. Store secrets in Secret Manager
4. Build and push container images
5. Deploy backend to Cloud Run
6. Deploy frontend to Cloud Run
7. Configure Cloud SQL connection
8. Set up Cloud Build for CI/CD

## Skills Tested
- GCP console and gcloud CLI
- Cloud Run deployment
- Cloud SQL management
- Secret Manager usage
- CI/CD with Cloud Build
- IAM and service accounts
