# Setup Guides

This directory contains detailed installation and configuration guides for all the tools and software required for the Java FSE with Angular training program.

## Required Software

### Week 1-2 Requirements
- [ ] Java JDK 21
- [ ] IntelliJ IDEA or Eclipse
- [ ] Git
- [ ] MySQL Server 8.x
- [ ] MySQL Workbench (optional but recommended)

### Week 2-6 Requirements
- [ ] Node.js (LTS version)
- [ ] npm (comes with Node.js)
- [ ] Visual Studio Code
- [ ] Postman or similar API testing tool

### Week 11-13 Requirements
- [ ] Docker Desktop
- [ ] kubectl (Kubernetes CLI)
- [ ] Google Cloud SDK (gcloud CLI)
- [ ] GCP Account (with free tier)

## Setup Order

Follow this recommended order for setting up your development environment:

1. **Java JDK** - Core requirement for all Java development
2. **IDE (IntelliJ IDEA / Eclipse)** - For Java development
3. **Git** - Version control for all projects
4. **MySQL** - Database for Projects 1-3
5. **Node.js & npm** - Required for Angular and frontend tools
6. **Visual Studio Code** - For frontend development
7. **Postman** - API testing
8. **Docker** - Containerization (Week 11)
9. **GCP Setup** - Cloud deployment (Week 12)

## Quick Installation Commands

### macOS (using Homebrew)
```bash
# Install Homebrew if not already installed
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Install Java
brew install openjdk@21

# Install Git
brew install git

# Install MySQL
brew install mysql

# Install Node.js
brew install node

# Install Docker
brew install --cask docker

# Install VS Code
brew install --cask visual-studio-code

# Install IntelliJ IDEA Community Edition
brew install --cask intellij-idea-ce

# Install Google Cloud SDK
brew install --cask google-cloud-sdk
```

### Windows (using Chocolatey)
```powershell
# Install Chocolatey (run as Administrator)
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

# Install Java
choco install openjdk21

# Install Git
choco install git

# Install MySQL
choco install mysql

# Install Node.js
choco install nodejs

# Install Docker Desktop
choco install docker-desktop

# Install VS Code
choco install vscode

# Install IntelliJ IDEA Community
choco install intellijidea-community

# Install Google Cloud SDK
choco install gcloudsdk
```

### Linux (Ubuntu/Debian)
```bash
# Update package index
sudo apt update

# Install Java
sudo apt install openjdk-21-jdk

# Install Git
sudo apt install git

# Install MySQL
sudo apt install mysql-server

# Install Node.js
curl -fsSL https://deb.nodesource.com/setup_lts.x | sudo -E bash -
sudo apt install -y nodejs

# Install Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Install VS Code
sudo snap install code --classic

# Install Google Cloud SDK
echo "deb [signed-by=/usr/share/keyrings/cloud.google.gpg] https://packages.cloud.google.com/apt cloud-sdk main" | sudo tee -a /etc/apt/sources.list.d/google-cloud-sdk.list
curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key --keyring /usr/share/keyrings/cloud.google.gpg add -
sudo apt update && sudo apt install google-cloud-sdk
```

## Verification Commands

After installation, verify each tool is working correctly:

```bash
# Java
java -version
javac -version

# Git
git --version

# MySQL
mysql --version

# Node.js and npm
node --version
npm --version

# Docker
docker --version
docker ps

# kubectl
kubectl version --client

# gcloud
gcloud --version
```

## Detailed Setup Guides

For detailed step-by-step instructions with screenshots, refer to the individual setup guides:

- [Java JDK Setup Guide](./java-setup.md)
- [IDE Setup Guide](./ide-setup.md)
- [MySQL Installation Guide](./mysql-setup.md)
- [Node.js Setup Guide](./nodejs-setup.md)
- [Docker Installation Guide](./docker-setup.md)
- [Git Configuration Guide](./git-setup.md)
- [GCP Account Setup Guide](./gcp-setup.md)

## Troubleshooting

### Common Issues

**Java not found in PATH**
- Windows: Add Java bin directory to System Environment Variables
- macOS/Linux: Add to `.bash_profile` or `.zshrc`

**MySQL Connection Issues**
- Check if MySQL service is running
- Verify username and password
- Check port (default: 3306)

**npm Permission Issues (Linux/macOS)**
- Don't use sudo with npm
- Fix permissions: `npm config set prefix ~/.npm-global`
- Add to PATH: `export PATH=~/.npm-global/bin:$PATH`

**Docker Desktop not starting**
- Ensure virtualization is enabled in BIOS
- Check system requirements
- Try restarting the service

## Additional Tools (Optional but Recommended)

- **Postman** - API testing and development
- **DBeaver** or **MySQL Workbench** - Database management GUI
- **Angular CLI** - `npm install -g @angular/cli`
- **Maven** - Java build tool (often included with IDEs)
- **Lombok** - Reduce boilerplate code (IDE plugin)
- **GitKraken** or **SourceTree** - Git GUI clients

## Getting Help

If you encounter issues during setup:
1. Check the official documentation for the specific tool
2. Search for error messages online (Stack Overflow)
3. Ask your instructor or peers
4. Consult the course discussion forum

## Next Steps

Once all required software is installed and verified:
1. Test your setup with a simple "Hello World" program
2. Configure your IDE with necessary plugins
3. Set up your Git credentials
4. Create a test database in MySQL
5. Start with [Module 1: Linux Fundamentals](../../01-linux/)

---

**Important**: Complete all setup before the course begins to avoid delays in learning!
