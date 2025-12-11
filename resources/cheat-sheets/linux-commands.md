# Linux Commands Cheat Sheet

## Navigation & Directory Operations

```bash
# Print working directory
pwd

# List files and directories
ls                  # Basic listing
ls -l               # Long format (permissions, size, date)
ls -a               # Show hidden files
ls -la              # Long format with hidden files
ls -lh              # Human-readable file sizes
ls -lt              # Sort by modification time
ls -lS              # Sort by file size

# Change directory
cd /path/to/dir     # Go to specific directory
cd ~                # Go to home directory
cd ..               # Go up one directory
cd -                # Go to previous directory
cd                  # Go to home directory

# Create directory
mkdir dirname       # Create single directory
mkdir -p a/b/c      # Create nested directories

# Remove directory
rmdir dirname       # Remove empty directory
rm -r dirname       # Remove directory and contents
rm -rf dirname      # Force remove (be careful!)
```

## File Operations

```bash
# Create file
touch filename      # Create empty file or update timestamp

# Copy files
cp source dest      # Copy file
cp -r dir1 dir2     # Copy directory recursively
cp -i source dest   # Interactive (prompt before overwrite)
cp -v source dest   # Verbose (show what's being copied)

# Move/Rename files
mv source dest      # Move or rename file
mv file1 file2 dir/ # Move multiple files to directory
mv -i source dest   # Interactive mode

# Remove files
rm filename         # Remove file
rm -f filename      # Force remove without confirmation
rm -i filename      # Interactive mode
rm *.txt            # Remove all .txt files

# View file contents
cat filename        # Display entire file
cat -n filename     # Display with line numbers
less filename       # View file page by page (q to quit)
more filename       # View file page by page
head filename       # First 10 lines
head -n 20 file     # First 20 lines
tail filename       # Last 10 lines
tail -n 20 file     # Last 20 lines
tail -f filename    # Follow file (live updates)
```

## File Permissions

```bash
# Permission format: rwxrwxrwx (user group other)
# r=read(4), w=write(2), x=execute(1)

# Change permissions
chmod 755 file      # rwxr-xr-x
chmod 644 file      # rw-r--r--
chmod +x file       # Add execute permission
chmod -w file       # Remove write permission
chmod u+x file      # Add execute for user
chmod g+w file      # Add write for group
chmod o-r file      # Remove read for others

# Change ownership
chown user file     # Change owner
chown user:group file # Change owner and group
chgrp group file    # Change group

# View permissions
ls -l filename      # See permissions
stat filename       # Detailed file information
```

## Search & Find

```bash
# Find files
find /path -name "filename"           # Find by name
find . -name "*.txt"                  # Find all .txt files
find . -type f                        # Find all files
find . -type d                        # Find all directories
find . -mtime -7                      # Modified in last 7 days
find . -size +10M                     # Files larger than 10MB
find . -name "*.log" -delete          # Find and delete

# Search in files
grep "pattern" file                   # Search for pattern
grep -i "pattern" file                # Case-insensitive search
grep -r "pattern" dir/                # Recursive search
grep -n "pattern" file                # Show line numbers
grep -v "pattern" file                # Invert match (exclude)
grep -c "pattern" file                # Count matches
grep -l "pattern" *.txt               # Show only filenames

# Which command
which command       # Show path to command
whereis command     # Show binary, source, manual locations
```

## Text Processing

```bash
# Word count
wc filename         # Lines, words, characters
wc -l filename      # Count lines only
wc -w filename      # Count words only
wc -c filename      # Count characters only

# Sort
sort filename       # Sort alphabetically
sort -r filename    # Reverse sort
sort -n filename    # Numeric sort
sort -u filename    # Sort and remove duplicates

# Unique
uniq filename       # Remove duplicate adjacent lines
uniq -c filename    # Count occurrences
uniq -d filename    # Show only duplicates

# Cut (extract columns)
cut -d',' -f1 file  # Extract first field (CSV)
cut -d':' -f1,3 file # Extract fields 1 and 3

# Sed (stream editor)
sed 's/old/new/' file           # Replace first occurrence
sed 's/old/new/g' file          # Replace all occurrences
sed -i 's/old/new/g' file       # Edit file in-place
sed -n '5,10p' file             # Print lines 5-10

# Awk
awk '{print $1}' file           # Print first column
awk -F',' '{print $1,$3}' file  # CSV, print columns 1 and 3
```

## Process Management

```bash
# View processes
ps                  # Current shell processes
ps aux              # All processes (detailed)
ps aux | grep java  # Find Java processes
top                 # Real-time process viewer
htop                # Interactive process viewer (if installed)

# Process information
pgrep process_name  # Find process ID by name
pidof process_name  # Find process ID

# Kill processes
kill PID            # Terminate process (SIGTERM)
kill -9 PID         # Force kill (SIGKILL)
killall process     # Kill all processes with name
pkill process       # Kill by process name

# Background processes
command &           # Run in background
jobs                # List background jobs
fg %1               # Bring job 1 to foreground
bg %1               # Resume job 1 in background
Ctrl+Z              # Suspend current process
nohup command &     # Run immune to hangups
```

## System Information

```bash
# System info
uname -a            # System information
hostname            # Computer name
whoami              # Current username
id                  # User ID and group ID
uptime              # System uptime
date                # Current date and time

# Disk usage
df -h               # Disk space (human-readable)
df -i               # Inode usage
du -h dir/          # Directory size
du -sh dir/         # Summary of directory size
du -h --max-depth=1 # Size of subdirectories (1 level)

# Memory usage
free -h             # Memory usage (human-readable)
free -m             # Memory in megabytes

# CPU info
lscpu               # CPU information
cat /proc/cpuinfo   # Detailed CPU info

# Network
ifconfig            # Network interfaces (older)
ip addr             # Network interfaces (newer)
ping host           # Test connectivity
netstat -tuln       # List open ports
ss -tuln            # Socket statistics
```

## Archives & Compression

```bash
# Tar (tape archive)
tar -cvf archive.tar dir/       # Create tar archive
tar -xvf archive.tar            # Extract tar archive
tar -tvf archive.tar            # List contents

# Tar with gzip
tar -czvf archive.tar.gz dir/   # Create compressed archive
tar -xzvf archive.tar.gz        # Extract compressed archive

# Tar with bzip2
tar -cjvf archive.tar.bz2 dir/  # Create bzip2 archive
tar -xjvf archive.tar.bz2       # Extract bzip2 archive

# Zip
zip archive.zip file1 file2     # Create zip
zip -r archive.zip dir/         # Zip directory
unzip archive.zip               # Extract zip
unzip -l archive.zip            # List contents

# Gzip
gzip file           # Compress (creates file.gz, removes original)
gzip -k file        # Keep original file
gunzip file.gz      # Decompress
```

## Input/Output Redirection

```bash
# Output redirection
command > file      # Redirect output (overwrite)
command >> file     # Redirect output (append)
command 2> file     # Redirect errors
command &> file     # Redirect output and errors
command > /dev/null # Discard output

# Input redirection
command < file      # Take input from file

# Pipes
command1 | command2 # Pass output to next command
cat file | grep pattern | sort
ps aux | grep java | wc -l

# Tee (write to file and stdout)
command | tee file  # Write to file and display
command | tee -a file # Append to file and display
```

## Networking

```bash
# Download files
wget url            # Download file
wget -O name url    # Save with custom name
curl url            # Display content
curl -O url         # Download file
curl -o name url    # Save with custom name

# SSH
ssh user@host       # Connect to remote host
ssh -p port user@host # Connect on specific port
ssh-keygen          # Generate SSH key pair

# SCP (secure copy)
scp file user@host:/path  # Copy to remote
scp user@host:/path/file . # Copy from remote
scp -r dir user@host:/path # Copy directory

# Rsync
rsync -avz source/ dest/  # Sync directories
rsync -avz file user@host:/path # Sync to remote
```

## Shortcuts & Tips

```bash
# Keyboard shortcuts
Ctrl+C              # Kill current process
Ctrl+Z              # Suspend current process
Ctrl+D              # Exit shell (EOF)
Ctrl+L              # Clear screen
Ctrl+A              # Move to line start
Ctrl+E              # Move to line end
Ctrl+U              # Delete line before cursor
Ctrl+K              # Delete line after cursor
Ctrl+W              # Delete word before cursor
Ctrl+R              # Search command history
Tab                 # Auto-complete

# History
history             # Show command history
!n                  # Run command number n
!!                  # Run last command
!string             # Run last command starting with string
!$                  # Last argument of previous command

# Aliases
alias ll='ls -la'   # Create alias
alias               # List all aliases
unalias ll          # Remove alias

# Environment variables
echo $PATH          # Display PATH
export VAR=value    # Set environment variable
env                 # List all environment variables
```

## File Viewing & Editing

```bash
# Text editors
nano filename       # Nano editor (beginner-friendly)
vim filename        # Vim editor
vi filename         # Vi editor

# Vim basics
i                   # Insert mode
Esc                 # Normal mode
:w                  # Save
:q                  # Quit
:wq                 # Save and quit
:q!                 # Quit without saving
/pattern            # Search
n                   # Next match
```

## System Administration

```bash
# User management
sudo command        # Run as superuser
su - username       # Switch user
passwd              # Change password
useradd username    # Add user
userdel username    # Delete user

# Package management (Ubuntu/Debian)
sudo apt update     # Update package list
sudo apt upgrade    # Upgrade packages
sudo apt install pkg # Install package
sudo apt remove pkg  # Remove package

# Package management (CentOS/RHEL)
sudo yum update     # Update packages
sudo yum install pkg # Install package
sudo yum remove pkg  # Remove package

# Services
sudo systemctl start service   # Start service
sudo systemctl stop service    # Stop service
sudo systemctl restart service # Restart service
sudo systemctl status service  # Check status
sudo systemctl enable service  # Enable at boot
```

## Quick Reference

```bash
# Most commonly used commands for developers
cd          # Change directory
ls -la      # List files with details
pwd         # Print working directory
mkdir       # Create directory
touch       # Create file
rm          # Remove file
cp          # Copy
mv          # Move/rename
cat         # View file
grep        # Search in files
find        # Find files
chmod       # Change permissions
ps aux      # View processes
kill        # Kill process
tar -xzvf   # Extract compressed archive
```

## Tips for Java Developers

```bash
# Find Java processes
ps aux | grep java
jps                 # Java process status (if JDK installed)

# Find port usage
lsof -i :8080       # What's using port 8080
netstat -tuln | grep 8080

# Monitor log files
tail -f application.log
tail -f catalina.out | grep ERROR

# Find large files (logs)
find . -type f -size +100M

# Quick file search
find . -name "*.java" | grep Service
```

---

**Pro Tips:**
- Use `man command` to see manual for any command
- Use `command --help` for quick help
- Use Tab for auto-completion
- Use Ctrl+R to search command history
- Chain commands with `&&` (execute if previous succeeds)
- Background long-running tasks with `&`
