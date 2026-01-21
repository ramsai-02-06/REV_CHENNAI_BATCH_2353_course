# Deploying Angular to S3

## Overview

Amazon S3 (Simple Storage Service) can host static websites, making it perfect for Angular applications. S3 is cost-effective and scales automatically.

```
┌─────────────────────────────────────────────────────────────┐
│  Full Stack Architecture                                     │
│                                                              │
│  Browser → S3 (Angular) → EC2 (Spring Boot API) → RDS       │
│              :80              :8080                :3306     │
└─────────────────────────────────────────────────────────────┘
```

---

## Why S3 for Angular?

| Feature | Benefit |
|---------|---------|
| **No server management** | No EC2 needed for frontend |
| **Auto-scaling** | Handles any traffic level |
| **Cost-effective** | Pay only for storage and requests |
| **High availability** | 99.99% uptime |
| **Free tier** | 5 GB storage, 20,000 GET requests/month |

---

## Prerequisites

- [ ] Angular application ready
- [ ] AWS account with S3 access
- [ ] AWS CLI configured (optional but recommended)

---

## Step 1: Build Angular for Production

### Build Command

```bash
# From Angular project root
ng build --configuration=production

# Or with npm
npm run build -- --configuration=production
```

### Output

Build creates `dist/your-app-name/` folder containing:
```
dist/your-app-name/
├── index.html
├── main.js
├── polyfills.js
├── runtime.js
├── styles.css
└── assets/
```

### Configure API URL

Before building, configure your API endpoint in `environment.prod.ts`:

```typescript
// src/environments/environment.prod.ts
export const environment = {
  production: true,
  apiUrl: 'http://your-ec2-public-ip:8080/api'
};
```

Use in services:
```typescript
import { environment } from '../environments/environment';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private baseUrl = environment.apiUrl;

  getItems() {
    return this.http.get(`${this.baseUrl}/items`);
  }
}
```

---

## Step 2: Create S3 Bucket

### Via AWS Console

```
AWS Console → S3 → Create bucket
```

### Bucket Configuration

```
Bucket name: my-angular-app-frontend
  (Must be globally unique)

Region: us-east-1 (same as your EC2/RDS)

Object Ownership: ACLs disabled

Block Public Access: UNCHECK "Block all public access"
  ⚠️ Acknowledge the warning (required for website hosting)

Versioning: Disabled (for learning)

Encryption: SSE-S3 (default)
```

Click **Create bucket**.

---

## Step 3: Enable Static Website Hosting

### Configure Hosting

```
S3 → your-bucket → Properties → Static website hosting → Edit

Static website hosting: Enable
Hosting type: Host a static website
Index document: index.html
Error document: index.html  ← Important for Angular routing!
```

**Save changes.**

### Why index.html for Error Document?

Angular uses client-side routing. When users navigate to `/products/123`:
1. Browser requests `/products/123` from S3
2. S3 doesn't find that file (404)
3. S3 serves `index.html` instead
4. Angular router handles the route

---

## Step 4: Configure Bucket Policy

Allow public read access to your bucket.

```
S3 → your-bucket → Permissions → Bucket policy → Edit
```

Add this policy (replace `YOUR-BUCKET-NAME`):

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "PublicReadGetObject",
      "Effect": "Allow",
      "Principal": "*",
      "Action": "s3:GetObject",
      "Resource": "arn:aws:s3:::YOUR-BUCKET-NAME/*"
    }
  ]
}
```

**Save changes.**

---

## Step 5: Upload Files

### Option 1: AWS Console

```
S3 → your-bucket → Upload → Add files

Select all files from dist/your-app-name/
Include the assets folder

Click Upload
```

### Option 2: AWS CLI (Recommended)

```bash
# Upload all files
aws s3 sync dist/your-app-name/ s3://your-bucket-name/

# With delete (removes old files)
aws s3 sync dist/your-app-name/ s3://your-bucket-name/ --delete
```

### Verify Upload

```
S3 → your-bucket → Objects

Should see:
  index.html
  main.js
  polyfills.js
  runtime.js
  styles.css
  assets/
```

---

## Step 6: Access Your Application

### Get Website URL

```
S3 → your-bucket → Properties → Static website hosting

Bucket website endpoint:
http://your-bucket-name.s3-website-us-east-1.amazonaws.com
```

### Test

Open the URL in browser. Your Angular app should load.

---

## Step 7: Configure CORS (for API Calls)

Your Angular app on S3 needs to call your Spring Boot API on EC2. Configure CORS on the **backend**.

### Spring Boot CORS Configuration

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins(
                "http://your-bucket.s3-website-us-east-1.amazonaws.com",
                "http://localhost:4200"  // For local development
            )
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*");
    }
}
```

Or per controller:

```java
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {
    "http://your-bucket.s3-website-us-east-1.amazonaws.com",
    "http://localhost:4200"
})
public class ItemController {
    // ...
}
```

---

## Updating Your Application

### Redeploy Steps

```bash
# 1. Build new version
ng build --configuration=production

# 2. Upload to S3 (replaces old files)
aws s3 sync dist/your-app-name/ s3://your-bucket-name/ --delete

# 3. Done! Changes are live immediately
```

### Deployment Script

Create `deploy.sh`:

```bash
#!/bin/bash
BUCKET_NAME="your-bucket-name"
DIST_PATH="dist/your-app-name"

echo "Building Angular..."
ng build --configuration=production

echo "Deploying to S3..."
aws s3 sync $DIST_PATH s3://$BUCKET_NAME/ --delete

echo "Deployment complete!"
echo "URL: http://$BUCKET_NAME.s3-website-us-east-1.amazonaws.com"
```

```bash
chmod +x deploy.sh
./deploy.sh
```

---

## Full Stack Architecture

```
┌────────────────────────────────────────────────────────────────┐
│                         Internet                                │
└───────────────────────────┬────────────────────────────────────┘
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
        ▼                   │                   │
┌───────────────┐           │           ┌──────────────────────┐
│      S3       │           │           │        VPC           │
│ ┌───────────┐ │           │           │  ┌────────────────┐  │
│ │  Angular  │ │───────────┼──────────▶│  │      EC2       │  │
│ │  (static) │ │  API calls│           │  │  Spring Boot   │  │
│ └───────────┘ │  :8080    │           │  │    :8080       │  │
│               │           │           │  └───────┬────────┘  │
│ Website URL   │           │           │          │           │
└───────────────┘           │           │          ▼           │
                            │           │  ┌────────────────┐  │
                            │           │  │      RDS       │  │
                            │           │  │     MySQL      │  │
                            │           │  └────────────────┘  │
                            │           └──────────────────────┘
                            │
                       User Browser
```

---

## Troubleshooting

### 403 Forbidden

| Check | Solution |
|-------|----------|
| Bucket policy | Ensure policy allows `s3:GetObject` |
| Block public access | Must be disabled |
| Object permissions | Files should be publicly readable |

### 404 Not Found

| Check | Solution |
|-------|----------|
| Index document | Set to `index.html` |
| File uploaded | Verify files exist in bucket |
| Correct URL | Using website endpoint, not bucket URL |

### Angular Routes Return 404

| Check | Solution |
|-------|----------|
| Error document | Set to `index.html` |
| Routing strategy | Use `PathLocationStrategy` (default) |

### CORS Errors

| Check | Solution |
|-------|----------|
| Backend CORS | Configure Spring Boot CORS |
| Allowed origins | Include S3 website URL |
| API URL | Correct EC2 IP in environment.prod.ts |

### API Calls Failing

```
Browser Console:
"Access to XMLHttpRequest blocked by CORS policy"
```

1. Check Spring Boot CORS configuration
2. Verify EC2 security group allows port 8080
3. Confirm API URL in Angular environment file

---

## Cost Considerations

### Free Tier (12 months)

| Resource | Free Allowance |
|----------|----------------|
| Storage | 5 GB |
| GET requests | 20,000/month |
| PUT requests | 2,000/month |

### Typical Angular App

- Size: 1-5 MB
- Monthly requests: Varies by traffic
- **Cost for small app: Usually free or < $1/month**

---

## S3 Website vs CloudFront

| Feature | S3 Website | S3 + CloudFront |
|---------|------------|-----------------|
| HTTPS | No (HTTP only) | Yes |
| Custom domain | Limited | Full support |
| Caching | No | Yes (edge locations) |
| Performance | Good | Excellent |
| Cost | Lower | Slightly higher |

For production, consider adding CloudFront for HTTPS and better performance.

---

## Summary

| Step | Action |
|------|--------|
| 1 | Build Angular: `ng build --configuration=production` |
| 2 | Create S3 bucket (unblock public access) |
| 3 | Enable static website hosting |
| 4 | Add bucket policy for public read |
| 5 | Upload dist files |
| 6 | Configure CORS on Spring Boot |

### Quick Reference

```bash
# Build
ng build --configuration=production

# Deploy
aws s3 sync dist/app-name/ s3://bucket-name/ --delete

# Website URL
http://bucket-name.s3-website-us-east-1.amazonaws.com
```

### Checklist

- [ ] Build Angular for production
- [ ] Create S3 bucket with public access
- [ ] Enable static website hosting
- [ ] Set error document to index.html
- [ ] Add bucket policy
- [ ] Upload files
- [ ] Configure CORS on backend
- [ ] Test full stack integration
