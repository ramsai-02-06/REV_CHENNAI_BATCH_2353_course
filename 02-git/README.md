# Git Version Control

## Overview
Git is a distributed version control system that enables developers to track changes, collaborate with team members, and manage code effectively. This module covers Git fundamentals and best practices for source control management.

## Learning Objectives
By the end of this module, you will be able to:
- Understand version control concepts
- Use Git for local and remote repository management
- Create branches and manage merges
- Collaborate using remote repositories
- Follow Git best practices and workflows

## Topics Covered

### 1. Git - Source Control Management
- What is version control?
- Centralized vs Distributed VCS
- Git architecture and concepts
- Git workflow overview

### 2. VCS, CVCS, DVCS
- **VCS** (Version Control System): Basic concepts
- **CVCS** (Centralized Version Control System): SVN, Perforce
- **DVCS** (Distributed Version Control System): Git, Mercurial

### 3. Git Fundamentals
- Repository initialization
- Staging area concept
- Committing changes
- Viewing history
- Working directory states

### 4. Git Installation and Configuration
- Installing Git on different platforms
- Initial configuration
  - Setting username and email
  - Configuring editor
  - Setting up SSH keys
- Verifying installation

### 5. Basic Git Commands
- `git init` - Initialize repository
- `git add` - Stage changes
- `git commit` - Save changes
- `git status` - Check repository status
- `git log` - View commit history
- `git diff` - View changes

### 6. Git Branching and Merging
- Understanding branches
- Creating and switching branches
- Branch strategies (feature branches, release branches)
- Merging branches
- Resolving merge conflicts
- Deleting branches

### 7. Remote Repositories
- Understanding remote repositories
- GitHub, GitLab, Bitbucket overview
- Cloning repositories
- Push and pull operations
- Fetching changes
- Remote branch management

### 8. Pull Requests
- What are pull requests?
- Creating pull requests
- Code review process
- Merging pull requests
- Pull request best practices

### 9. .gitignore
- Purpose of .gitignore
- Syntax and patterns
- Common files to ignore
- Language-specific ignore patterns
- Global vs local .gitignore

## Essential Git Commands

### Setup and Configuration
```bash
# Configure user information
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"

# View configuration
git config --list

# Initialize a repository
git init

# Clone a repository
git clone <repository-url>
```

### Basic Workflow
```bash
# Check status
git status

# Stage files
git add filename
git add .                # Stage all changes

# Commit changes
git commit -m "Commit message"
git commit -am "Message" # Stage and commit tracked files

# View history
git log
git log --oneline        # Compact view
git log --graph          # Visual representation

# View changes
git diff                 # Unstaged changes
git diff --staged        # Staged changes
```

### Branching
```bash
# List branches
git branch

# Create new branch
git branch branch-name

# Switch branch
git checkout branch-name
git switch branch-name   # Modern alternative

# Create and switch
git checkout -b branch-name

# Merge branch
git merge branch-name

# Delete branch
git branch -d branch-name
```

### Remote Operations
```bash
# View remotes
git remote -v

# Add remote
git remote add origin <url>

# Push changes
git push origin branch-name
git push -u origin branch-name  # Set upstream

# Pull changes
git pull origin branch-name

# Fetch changes
git fetch origin
```

## Git Workflow Strategies

### Feature Branch Workflow
1. Create feature branch from main
2. Make changes and commit
3. Push feature branch
4. Create pull request
5. Code review and merge
6. Delete feature branch

### Gitflow Workflow
- `main` - Production-ready code
- `develop` - Integration branch
- `feature/*` - Feature development
- `release/*` - Release preparation
- `hotfix/*` - Production fixes

## Exercises

### Exercise 1: Initialize and Basic Operations
1. Create a new directory called `my-first-repo`
2. Initialize it as a Git repository
3. Create a file `README.md` with some content
4. Check the status of your repository
5. Stage the file
6. Commit with message "Initial commit"
7. View the commit history

### Exercise 2: Working with Branches
1. Create a new branch called `feature/add-content`
2. Switch to the new branch
3. Create a file `index.html` with basic HTML structure
4. Commit the changes
5. Switch back to main branch
6. Merge the feature branch into main
7. Delete the feature branch

### Exercise 3: Remote Repository
1. Create a new repository on GitHub
2. Add the GitHub repository as a remote
3. Push your local repository to GitHub
4. Make changes to a file
5. Commit and push the changes
6. View your repository on GitHub

### Exercise 4: Collaboration Workflow
1. Clone a practice repository (use your own or a public one)
2. Create a feature branch
3. Make changes and commit
4. Push the feature branch
5. Create a pull request on GitHub
6. (If working in a team) Review someone else's pull request

### Exercise 5: .gitignore Practice
1. Create a new repository
2. Create files: `app.js`, `config.env`, `node_modules/` folder, `.DS_Store`
3. Create a `.gitignore` file
4. Configure it to ignore:
   - `config.env`
   - `node_modules/`
   - `.DS_Store`
   - All `.log` files
5. Verify that ignored files don't appear in `git status`

## Common Scenarios

### Undo Changes
```bash
# Discard unstaged changes
git checkout -- filename

# Unstage file
git reset HEAD filename

# Undo last commit (keep changes)
git reset --soft HEAD~1

# Undo last commit (discard changes)
git reset --hard HEAD~1
```

### Resolving Merge Conflicts
1. Git will mark conflicts in files
2. Open conflicted files
3. Look for conflict markers: `<<<<<<<`, `=======`, `>>>>>>>`
4. Edit file to resolve conflicts
5. Remove conflict markers
6. Stage resolved files
7. Complete merge with commit

## Best Practices

### Commit Messages
- Use imperative mood: "Add feature" not "Added feature"
- Keep subject line under 50 characters
- Separate subject from body with blank line
- Explain what and why, not how

**Good examples:**
```
Add user authentication module
Fix null pointer exception in service layer
Update dependencies to latest versions
```

### General Guidelines
- Commit often with logical units of work
- Pull before pushing
- Review changes before committing
- Use branches for new features
- Keep commits atomic and focused
- Don't commit sensitive information
- Use meaningful branch names

## Additional Resources

### Documentation
- [Official Git Documentation](https://git-scm.com/doc)
- [Pro Git Book](https://git-scm.com/book/en/v2)
- [GitHub Guides](https://guides.github.com/)

### Interactive Learning
- [Learn Git Branching](https://learngitbranching.js.org/)
- [Git Immersion](http://gitimmersion.com/)

### Cheat Sheets
- See [resources/cheat-sheets/git-commands.md](../resources/cheat-sheets/)

## Assessment

You should be comfortable with:
- [ ] Initializing and configuring Git repositories
- [ ] Creating commits with meaningful messages
- [ ] Creating and managing branches
- [ ] Merging branches and resolving conflicts
- [ ] Working with remote repositories
- [ ] Creating and reviewing pull requests
- [ ] Using .gitignore effectively

## Next Steps

Once you've mastered Git basics, proceed to:
- [Module 3: Agile Methodology](../03-agile/)

---

**Time Estimate:** 1 day
**Difficulty:** Beginner
**Prerequisites:** Linux command-line basics
