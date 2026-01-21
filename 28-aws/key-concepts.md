# AWS Key Concepts

## Overview

Essential AWS concepts for deploying a full-stack application: Angular on S3, Spring Boot on EC2, MySQL on RDS.

---

## 1. AWS Fundamentals

### Cloud Computing Model

```
On-Premises              Cloud (AWS)
─────────────            ───────────
Buy hardware    →        Rent resources
Setup months    →        Launch minutes
CapEx           →        OpEx (pay-as-you-go)
You maintain    →        AWS maintains
```

### Free Tier (12 Months)

| Service | Free Allowance |
|---------|----------------|
| S3 | 5 GB storage, 20K GET requests |
| EC2 | 750 hrs/month t2.micro |
| RDS | 750 hrs/month db.t3.micro |
| EBS | 30 GB storage |

### IAM Best Practices

```
Root Account → Create IAM Admin → Use IAM Admin Daily
                    ↓
              Enable MFA on both
```

---

## 2. EC2 (Elastic Compute Cloud)

### Key Components

| Component | Description |
|-----------|-------------|
| **Instance** | Virtual server |
| **AMI** | OS template (Amazon Linux, Ubuntu) |
| **Instance Type** | Hardware (t2.micro = 1 vCPU, 1GB RAM) |
| **Key Pair** | SSH authentication |
| **Security Group** | Firewall rules |

### Launch Instance (Quick Reference)

```
AMI: Amazon Linux 2023
Instance Type: t2.micro (free tier)
Key Pair: Create new → Download .pem
Security Group: SSH (22), HTTP (80), 8080
Public IP: Enable
```

### SSH Connection

```bash
chmod 400 my-key.pem
ssh -i my-key.pem ec2-user@<PUBLIC-IP>
```

---

## 3. RDS (Relational Database Service)

### Why RDS?

| Self-Managed | RDS |
|--------------|-----|
| Install database | Pre-configured |
| Manual backups | Automatic backups |
| Apply patches | Automatic patching |
| Scale manually | Easy scaling |

### Create Instance (Quick Reference)

```
Engine: MySQL 8.0
Template: Free tier
Instance: db.t3.micro
Storage: 20 GB (no autoscaling)
Public Access: Yes (for setup) → No (for production)
```

### Connection String

```
jdbc:mysql://<ENDPOINT>:3306/<DATABASE>

Example:
jdbc:mysql://mydb.abc123.us-east-1.rds.amazonaws.com:3306/myappdb
```

---

## 4. Security Groups

### EC2 Security Group

| Type | Port | Source | Purpose |
|------|------|--------|---------|
| SSH | 22 | My IP | Admin access |
| HTTP | 80 | 0.0.0.0/0 | Web traffic |
| Custom | 8080 | 0.0.0.0/0 | Spring Boot |

### RDS Security Group

| Type | Port | Source | Purpose |
|------|------|--------|---------|
| MySQL | 3306 | EC2-SG-ID | From EC2 only |

### Key Rule: Reference Security Groups

```
RDS inbound: Allow MySQL from sg-xxxxx (EC2 security group)
NOT from IP address
```

---

## 5. Deployment

### Build and Transfer

```bash
# Build
./mvnw clean package -DskipTests

# Transfer
scp -i key.pem target/app.jar ec2-user@IP:/opt/myapp/
```

### Environment Variables

```bash
# /opt/myapp/.env
DB_HOST=mydb.abc123.us-east-1.rds.amazonaws.com
DB_NAME=myappdb
DB_USERNAME=admin
DB_PASSWORD=secret
```

### Systemd Service

```ini
# /etc/systemd/system/myapp.service
[Unit]
Description=Spring Boot App
After=network.target

[Service]
Type=simple
User=ec2-user
EnvironmentFile=/opt/myapp/.env
ExecStart=/usr/bin/java -jar /opt/myapp/app.jar
Restart=on-failure

[Install]
WantedBy=multi-user.target
```

### Service Commands

```bash
sudo systemctl start myapp     # Start
sudo systemctl stop myapp      # Stop
sudo systemctl restart myapp   # Restart
sudo systemctl status myapp    # Status
journalctl -u myapp -f         # Logs
```

---

## 6. S3 Static Website Hosting

### Why S3 for Angular?

| Feature | Benefit |
|---------|---------|
| No server | No EC2 needed for frontend |
| Auto-scaling | Handles any traffic |
| Cost-effective | Pay for storage/requests |
| High availability | 99.99% uptime |

### Setup Steps

```
1. Create S3 bucket (globally unique name)
2. Unblock public access
3. Enable static website hosting
4. Set index.html and error document
5. Add bucket policy for public read
6. Upload Angular dist files
```

### Bucket Policy

```json
{
  "Version": "2012-10-17",
  "Statement": [{
    "Effect": "Allow",
    "Principal": "*",
    "Action": "s3:GetObject",
    "Resource": "arn:aws:s3:::BUCKET-NAME/*"
  }]
}
```

### Angular Routing Fix

Set **error document** to `index.html` so Angular router handles all routes.

### Deploy Commands

```bash
# Build Angular
ng build --configuration=production

# Upload to S3
aws s3 sync dist/app-name/ s3://bucket-name/ --delete
```

### Website URL

```
http://bucket-name.s3-website-us-east-1.amazonaws.com
```

### CORS (Spring Boot)

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("http://bucket.s3-website-us-east-1.amazonaws.com")
            .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
```

---

## Quick Reference

### AWS CLI Setup

```bash
aws configure
# Access Key: AKIA...
# Secret Key: ...
# Region: us-east-1
# Output: json
```

### Common Commands

```bash
# S3
aws s3 ls
aws s3 sync dist/ s3://bucket-name/ --delete
aws s3 rm s3://bucket-name/ --recursive

# EC2
aws ec2 describe-instances
aws ec2 start-instances --instance-ids i-xxx
aws ec2 stop-instances --instance-ids i-xxx

# RDS
aws rds describe-db-instances
```

### Deployment Script

```bash
#!/bin/bash
# deploy.sh
./mvnw clean package -DskipTests
scp -i key.pem target/*.jar ec2-user@$IP:/opt/myapp/app.jar
ssh -i key.pem ec2-user@$IP "sudo systemctl restart myapp"
```

---

## Architecture Diagram

```
Browser
   │
   ├──────────────────────────────────────────────────────┐
   │                                                      │
   ▼                                                      ▼
┌─────────────┐                           ┌────────────────────────────────┐
│     S3      │                           │           AWS VPC              │
│ ┌─────────┐ │    API calls (:8080)      │                                │
│ │ Angular │ │──────────────────────────▶│   ┌──────────┐   ┌──────────┐ │
│ └─────────┘ │                           │   │   EC2    │──▶│   RDS    │ │
└─────────────┘                           │   │  :8080   │   │  :3306   │ │
                                          │   └──────────┘   └──────────┘ │
                                          └────────────────────────────────┘
```

---

## Checklist

### Initial Setup
- [ ] Create AWS account
- [ ] Set billing alerts
- [ ] Create IAM admin user
- [ ] Enable MFA
- [ ] Install AWS CLI

### EC2 Setup
- [ ] Launch t2.micro instance
- [ ] Create key pair
- [ ] Configure security group (22, 80, 8080)
- [ ] Connect via SSH
- [ ] Install Java 17

### RDS Setup
- [ ] Create db.t3.micro instance
- [ ] Note endpoint and credentials
- [ ] Configure security group (allow EC2)
- [ ] Test connection from EC2

### Backend Deployment
- [ ] Build JAR locally
- [ ] Transfer to EC2
- [ ] Create environment file
- [ ] Create systemd service
- [ ] Start and verify

### Frontend Deployment (S3)
- [ ] Build Angular for production
- [ ] Create S3 bucket
- [ ] Enable static website hosting
- [ ] Set error document to index.html
- [ ] Add bucket policy
- [ ] Upload dist files
- [ ] Configure CORS on backend

---

## Troubleshooting

| Issue | Check |
|-------|-------|
| Can't SSH | Security group allows port 22 from your IP |
| Can't connect to RDS | RDS security group allows EC2 security group |
| App not accessible | Security group allows port 8080 |
| App crashes | Check logs: `journalctl -u myapp` |
| Wrong Java version | Install Java 17: `sudo yum install java-17-amazon-corretto` |
| S3 403 Forbidden | Bucket policy allows public read, public access unblocked |
| Angular routes 404 | Error document set to index.html |
| CORS errors | Configure CORS on Spring Boot backend |
