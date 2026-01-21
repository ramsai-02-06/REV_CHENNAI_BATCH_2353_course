# AWS Fundamentals

## Overview

This module covers AWS essentials for deploying a full-stack application: Angular frontend on S3, Spring Boot backend on EC2, and MySQL database on RDS. Focus is on free tier services and practical deployment skills.

## Learning Objectives

By the end of this module, you will be able to:
- Set up an AWS account and configure IAM users
- Launch and manage EC2 instances
- Create and configure RDS databases
- Understand VPC networking and security groups
- Deploy a Spring Boot application to EC2
- Host an Angular application on S3

---

## Topics Covered

### 1. [AWS Introduction](./topics/01-aws-introduction.md)
Get started with AWS and understand core concepts.

- What is AWS and cloud computing
- Free tier overview and limits
- Account setup and billing alerts
- IAM users, roles, and security
- AWS CLI installation and configuration

### 2. [EC2 Basics](./topics/02-ec2-basics.md)
Launch and manage virtual servers.

- EC2 concepts (AMI, instance types, key pairs)
- Launching an EC2 instance (t2.micro)
- Connecting via SSH
- Security groups for EC2
- Installing Java and dependencies

### 3. [RDS Setup](./topics/03-rds-setup.md)
Create a managed MySQL/PostgreSQL database.

- RDS overview and benefits
- Creating an RDS instance (db.t3.micro)
- Connection configuration
- Security groups for database access
- Spring Boot database configuration

### 4. [Networking Essentials](./topics/04-networking-essentials.md)
Understand VPC and secure your resources.

- VPC and subnet basics
- Security groups in depth
- EC2 ↔ RDS communication
- Troubleshooting connectivity

### 5. [Deploying Spring Boot](./topics/05-deploying-spring-boot.md)
Deploy your backend to EC2.

- Building and packaging JAR
- Transferring files to EC2
- Environment variables for configuration
- Running as a systemd service
- Updating and monitoring

### 6. [Deploying Angular to S3](./topics/06-deploying-angular-s3.md)
Host your frontend on S3.

- Building Angular for production
- Creating S3 bucket with static website hosting
- Bucket policy for public access
- Handling Angular routing
- CORS configuration for API calls

---

## Topic Flow

```
┌─────────────────────┐
│ 1. AWS Introduction │  Account, IAM, CLI
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ 2. EC2 Basics       │  Launch virtual server
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ 3. RDS Setup        │  Create database
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ 4. Networking       │  Security groups
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ 5. Deploy Backend   │  Spring Boot on EC2
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ 6. Deploy Frontend  │  Angular on S3
└─────────────────────┘
```

---

## Architecture

```
                         Internet
                             │
           ┌─────────────────┼─────────────────┐
           │                 │                 │
           ▼                 │                 ▼
   ┌───────────────┐         │         ┌─────────────────┐
   │      S3       │         │         │ Internet Gateway│
   │ ┌───────────┐ │         │         └────────┬────────┘
   │ │  Angular  │ │         │                  │
   │ │  (static) │ │─────────┼─────────────────▶│
   │ └───────────┘ │  API    │    ┌─────────────┴─────────────┐
   └───────────────┘  calls  │    │           VPC             │
                             │    │                           │
                             │    │  ┌─────────────────────┐  │
                        User │    │  │     EC2 (t2.micro)  │  │
                             │    │  │   Spring Boot :8080 │  │
                             │    │  └──────────┬──────────┘  │
                             │    │             │ port 3306   │
                             │    │             ▼             │
                             │    │  ┌─────────────────────┐  │
                             │    │  │   RDS (db.t3.micro) │  │
                             │    │  │       MySQL         │  │
                             │    │  └─────────────────────┘  │
                             │    └───────────────────────────┘
                             │
                         Browser
```

---

## Key Concepts Summary

| Concept | Description |
|---------|-------------|
| **S3** | Simple Storage Service (static file hosting) |
| **EC2** | Virtual servers (Elastic Compute Cloud) |
| **RDS** | Managed relational database service |
| **VPC** | Virtual Private Cloud (your network) |
| **Security Group** | Firewall rules for instances |
| **IAM** | Identity and Access Management |
| **AMI** | Amazon Machine Image (OS template) |

---

## Free Tier Limits

| Service | Free Allowance | Duration |
|---------|----------------|----------|
| S3 | 5 GB storage, 20K GET requests | 12 months |
| EC2 | 750 hrs/month t2.micro | 12 months |
| RDS | 750 hrs/month db.t3.micro | 12 months |
| EBS | 30 GB storage | 12 months |

**Set billing alerts to avoid unexpected charges!**

---

## Prerequisites

- AWS account (free tier)
- Spring Boot application to deploy
- Angular application to deploy
- Basic Linux command line knowledge
- SSH client installed

## Additional Resources

- [AWS Free Tier](https://aws.amazon.com/free/)
- [S3 Documentation](https://docs.aws.amazon.com/s3/)
- [EC2 Documentation](https://docs.aws.amazon.com/ec2/)
- [RDS Documentation](https://docs.aws.amazon.com/rds/)
- [AWS CLI Reference](https://docs.aws.amazon.com/cli/)

---

## Next Steps

After completing this module, consider learning:
- Elastic Load Balancing for high availability
- S3 for file storage
- CI/CD with CodePipeline
- Docker deployment on ECS

---

**Duration:** 3 days | **Difficulty:** Intermediate | **Prerequisites:** Spring Boot, Linux basics
