# Linux Fundamentals

## Overview
This module introduces you to Linux operating system fundamentals and basic command-line operations. Understanding Linux is essential for developers as most servers and cloud environments run on Linux.

## Learning Objectives
By the end of this module, you will be able to:
- Understand the Linux operating system architecture
- Navigate the Linux file system using command-line interface
- Execute basic Linux commands for file and directory operations
- Use shell scripting fundamentals
- Understand the "Fullstack the Big Picture" concept

## Topics Covered

### 1. Introduction to Linux OS
- What is Linux?
- Linux distributions
- Linux architecture
- Why Linux for developers?

### 2. Basic Linux Commands
- File system navigation (`cd`, `pwd`, `ls`)
- File operations (`cp`, `mv`, `rm`, `touch`)
- Directory operations (`mkdir`, `rmdir`)
- Viewing file contents (`cat`, `less`, `more`, `head`, `tail`)
- File permissions and ownership

### 3. Shell Scripting Fundamentals
- Introduction to Bash shell
- Creating and executing shell scripts
- Variables and data types
- Basic control structures
- Input/output operations

### 4. Fullstack the Big Picture
- Understanding the complete application stack
- Role of Linux in the development ecosystem
- Server-side considerations

## Key Commands Reference

```bash
# Navigation
pwd                 # Print working directory
ls                  # List directory contents
ls -la              # List all with details
cd /path/to/dir     # Change directory
cd ~                # Go to home directory
cd ..               # Go up one directory

# File Operations
touch filename      # Create empty file
cat filename        # Display file contents
cp source dest      # Copy file
mv source dest      # Move/rename file
rm filename         # Remove file
mkdir dirname       # Create directory
rmdir dirname       # Remove empty directory
rm -r dirname       # Remove directory recursively

# File Permissions
chmod 755 file      # Change file permissions
chown user file     # Change file owner
ls -l               # View permissions

# Text Processing
grep pattern file   # Search for pattern
find /path -name    # Find files
wc filename         # Word count
head -n 10 file     # First 10 lines
tail -n 10 file     # Last 10 lines

# System Information
whoami              # Current user
hostname            # System hostname
uname -a            # System information
df -h               # Disk space
ps aux              # Running processes
```

## Exercises

### Exercise 1: File System Navigation
1. Open a terminal
2. Print your current directory
3. List all files including hidden files
4. Navigate to your home directory
5. Create a directory called `devtraining`
6. Navigate into the `devtraining` directory

### Exercise 2: File Operations
1. Create three empty files: `file1.txt`, `file2.txt`, `file3.txt`
2. Add some text to `file1.txt` using echo or a text editor
3. Copy `file1.txt` to `file1_backup.txt`
4. Rename `file2.txt` to `file2_renamed.txt`
5. Delete `file3.txt`
6. List all files to verify your operations

### Exercise 3: Working with Permissions
1. Create a file called `script.sh`
2. Check its current permissions using `ls -l`
3. Make the file executable using `chmod`
4. Verify the permission change

### Exercise 4: Basic Shell Script
Create a shell script that:
1. Prints "Hello, Full Stack Developer!"
2. Creates a directory called `logs`
3. Creates a file inside `logs` called `app.log`
4. Writes current date and time to `app.log`

**Solution template:**
```bash
#!/bin/bash
# Your code here
```

## Additional Resources

### Documentation
- [Linux Documentation Project](https://www.tldp.org/)
- [GNU Bash Manual](https://www.gnu.org/software/bash/manual/)
- [Linux Command Line Basics](https://ubuntu.com/tutorials/command-line-for-beginners)

### Practice Platforms
- [Linux Journey](https://linuxjourney.com/)
- [OverTheWire - Bandit](https://overthewire.org/wargames/bandit/)

### Cheat Sheets
- See [resources/cheat-sheets/linux-commands.md](../resources/cheat-sheets/)

## Assessment

You should be comfortable with:
- [ ] Navigating the Linux file system
- [ ] Creating, moving, and deleting files and directories
- [ ] Understanding file permissions
- [ ] Writing basic shell scripts
- [ ] Using command-line text editors (nano, vim, or vi)

## Next Steps

Once you've completed this module and feel confident with Linux basics, proceed to:
- [Module 2: Git Version Control](../02-git/)

---

**Time Estimate:** 1 day
**Difficulty:** Beginner
**Prerequisites:** None
