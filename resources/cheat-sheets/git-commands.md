# Git Commands Cheat Sheet

## Setup & Configuration

```bash
# Install Git
# Ubuntu/Debian: sudo apt install git
# macOS: brew install git
# Windows: Download from git-scm.com

# Configure user information
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"

# View configuration
git config --list
git config user.name
git config user.email

# Configure default editor
git config --global core.editor "code --wait"  # VS Code
git config --global core.editor "nano"          # Nano
git config --global core.editor "vim"           # Vim

# Configure line endings
git config --global core.autocrlf true    # Windows
git config --global core.autocrlf input   # Mac/Linux

# View config file location
git config --list --show-origin
```

## Repository Basics

```bash
# Initialize repository
git init                    # Create new repo in current directory
git init project-name       # Create new repo in new directory

# Clone repository
git clone <url>             # Clone remote repo
git clone <url> folder-name # Clone into specific folder
git clone -b branch <url>   # Clone specific branch

# Repository status
git status                  # Show working tree status
git status -s               # Short format
git status -sb              # Short with branch info
```

## Making Changes

```bash
# Add files to staging
git add filename            # Stage specific file
git add .                   # Stage all changes
git add *.java              # Stage all Java files
git add -A                  # Stage all (new, modified, deleted)
git add -u                  # Stage modified and deleted only
git add -p                  # Interactive staging (review changes)

# Commit changes
git commit -m "message"     # Commit with message
git commit -am "message"    # Stage and commit tracked files
git commit                  # Open editor for commit message
git commit --amend          # Modify last commit
git commit --amend --no-edit # Amend without changing message

# View changes
git diff                    # Unstaged changes
git diff --staged           # Staged changes
git diff --cached           # Same as --staged
git diff HEAD               # All changes (staged + unstaged)
git diff filename           # Changes in specific file
git diff commit1 commit2    # Difference between commits
git diff branch1 branch2    # Difference between branches
```

## Viewing History

```bash
# View commit history
git log                     # Full commit history
git log --oneline           # Compact view
git log --graph             # Visual graph
git log --all --decorate --oneline --graph  # Detailed graph
git log -n 5                # Last 5 commits
git log --since="2 weeks ago"
git log --after="2024-01-01"
git log --author="John"
git log --grep="bugfix"     # Search commit messages
git log filename            # History of specific file

# Show commit details
git show commit-hash        # Show specific commit
git show HEAD               # Show last commit
git show HEAD~2             # Show 2 commits ago

# View file at specific commit
git show commit-hash:filename

# Blame (line-by-line history)
git blame filename          # Show who changed each line
git blame -L 10,20 filename # Lines 10-20 only
```

## Branches

```bash
# List branches
git branch                  # Local branches
git branch -r               # Remote branches
git branch -a               # All branches
git branch -v               # With last commit

# Create branch
git branch branch-name      # Create branch
git checkout -b branch-name # Create and switch
git switch -c branch-name   # Create and switch (newer)

# Switch branches
git checkout branch-name    # Switch branch
git switch branch-name      # Switch branch (newer)
git checkout -              # Switch to previous branch

# Rename branch
git branch -m old-name new-name     # Rename
git branch -m new-name              # Rename current branch

# Delete branch
git branch -d branch-name   # Delete (safe - checks if merged)
git branch -D branch-name   # Force delete
git push origin --delete branch-name # Delete remote branch

# Track remote branch
git branch --set-upstream-to=origin/branch
git branch -u origin/branch
```

## Merging

```bash
# Merge branch
git merge branch-name       # Merge branch into current
git merge --no-ff branch    # Create merge commit (no fast-forward)
git merge --squash branch   # Squash all commits into one

# Abort merge
git merge --abort           # Cancel merge in progress

# Resolve conflicts
# 1. Edit conflicted files
# 2. Remove conflict markers (<<<<, ====, >>>>)
# 3. Add resolved files: git add filename
# 4. Complete merge: git commit
```

## Remote Repositories

```bash
# View remotes
git remote                  # List remotes
git remote -v               # List with URLs
git remote show origin      # Detailed info

# Add remote
git remote add origin <url>
git remote add upstream <url>

# Change remote URL
git remote set-url origin <new-url>

# Remove remote
git remote remove origin

# Fetch changes
git fetch                   # Fetch all remotes
git fetch origin            # Fetch specific remote
git fetch origin branch     # Fetch specific branch

# Pull changes
git pull                    # Fetch and merge
git pull origin branch      # Pull specific branch
git pull --rebase           # Pull and rebase instead of merge

# Push changes
git push                    # Push current branch
git push origin branch      # Push specific branch
git push -u origin branch   # Push and set upstream
git push --all              # Push all branches
git push --tags             # Push all tags
git push --force            # Force push (dangerous!)
git push --force-with-lease # Safer force push
```

## Undoing Changes

```bash
# Discard changes in working directory
git checkout -- filename    # Discard changes in file
git checkout .              # Discard all changes
git restore filename        # Discard changes (newer)
git restore .               # Discard all changes

# Unstage files
git reset HEAD filename     # Unstage specific file
git reset                   # Unstage all files
git restore --staged file   # Unstage file (newer)

# Undo commits
git reset --soft HEAD~1     # Undo commit, keep changes staged
git reset HEAD~1            # Undo commit, keep changes unstaged
git reset --hard HEAD~1     # Undo commit, discard changes
git reset --hard commit-hash # Reset to specific commit

# Revert commit (safe)
git revert commit-hash      # Create new commit that undoes changes
git revert HEAD             # Revert last commit

# Clean untracked files
git clean -n                # Dry run (show what will be deleted)
git clean -f                # Delete untracked files
git clean -fd               # Delete untracked files and directories
git clean -fX               # Delete ignored files
git clean -fx               # Delete all untracked files
```

## Stashing

```bash
# Stash changes
git stash                   # Stash changes
git stash save "message"    # Stash with message
git stash -u                # Include untracked files
git stash --include-untracked
git stash --all             # Include ignored files

# View stashes
git stash list              # List all stashes
git stash show              # Show latest stash
git stash show stash@{0}    # Show specific stash
git stash show -p           # Show diff

# Apply stash
git stash apply             # Apply latest stash
git stash apply stash@{1}   # Apply specific stash
git stash pop               # Apply and remove stash
git stash pop stash@{1}     # Pop specific stash

# Delete stash
git stash drop              # Delete latest stash
git stash drop stash@{1}    # Delete specific stash
git stash clear             # Delete all stashes
```

## Tags

```bash
# List tags
git tag                     # List all tags
git tag -l "v1.*"           # List tags matching pattern

# Create tag
git tag v1.0.0              # Lightweight tag
git tag -a v1.0.0 -m "Version 1.0.0" # Annotated tag
git tag v1.0.0 commit-hash  # Tag specific commit

# Show tag
git show v1.0.0             # Show tag details

# Push tags
git push origin v1.0.0      # Push specific tag
git push origin --tags      # Push all tags

# Delete tag
git tag -d v1.0.0           # Delete local tag
git push origin --delete v1.0.0 # Delete remote tag
```

## Rebasing

```bash
# Rebase
git rebase branch-name      # Rebase current branch onto branch
git rebase master           # Rebase onto master
git rebase --continue       # Continue after resolving conflicts
git rebase --abort          # Cancel rebase
git rebase --skip           # Skip current commit

# Interactive rebase
git rebase -i HEAD~3        # Rebase last 3 commits
git rebase -i commit-hash   # Rebase from specific commit

# Interactive rebase commands:
# pick = use commit
# reword = use commit, but edit message
# edit = use commit, but stop for amending
# squash = combine with previous commit
# fixup = like squash, but discard message
# drop = remove commit
```

## .gitignore

```bash
# Create .gitignore file
# Add patterns to ignore files

# Common patterns:
*.log                       # All log files
*.class                     # All class files
/target/                    # Target directory
node_modules/               # Node modules
.env                        # Environment files
*.swp                       # Vim swap files
.DS_Store                   # macOS files

# Negation
!important.log              # Don't ignore this file

# Comments
# This is a comment

# View ignored files
git status --ignored

# Remove cached files (already tracked)
git rm --cached filename
git rm -r --cached .        # Remove all cached files
# Then commit the changes
```

## Collaborative Workflow

```bash
# Fork workflow
# 1. Fork repo on GitHub
# 2. Clone your fork
git clone <your-fork-url>

# 3. Add upstream remote
git remote add upstream <original-repo-url>

# 4. Create feature branch
git checkout -b feature-branch

# 5. Make changes and commit
git add .
git commit -m "Add feature"

# 6. Push to your fork
git push origin feature-branch

# 7. Create Pull Request on GitHub

# 8. Keep fork updated
git fetch upstream
git checkout main
git merge upstream/main
git push origin main
```

## Useful Aliases

```bash
# Add to ~/.gitconfig or use git config --global

[alias]
    st = status
    co = checkout
    br = branch
    ci = commit
    unstage = reset HEAD --
    last = log -1 HEAD
    visual = log --all --decorate --oneline --graph
    lg = log --graph --pretty=format:'%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset'
    undo = reset --soft HEAD~1
    amend = commit --amend --no-edit

# Use as: git st, git co, etc.
```

## Troubleshooting

```bash
# View what changed
git whatchanged             # Show changes with commit info

# Find when bug was introduced
git bisect start            # Start bisect
git bisect bad              # Mark current as bad
git bisect good commit-hash # Mark known good commit
# Git will checkout commits for testing
git bisect good/bad         # Mark each commit
git bisect reset            # Finish bisect

# Find who deleted a file
git log --all --full-history -- "*/filename.*"

# Recover deleted branch
git reflog                  # Find commit hash
git checkout -b branch-name commit-hash

# Recover lost commits
git reflog                  # View reference logs
git reset --hard commit-hash

# Fix detached HEAD
git checkout branch-name    # Return to branch
# Or create new branch from detached state
git checkout -b new-branch
```

## Advanced Commands

```bash
# Cherry-pick (apply specific commit)
git cherry-pick commit-hash
git cherry-pick commit1 commit2

# Submodules
git submodule add <url> path
git submodule init
git submodule update

# Archive
git archive --format=zip HEAD > archive.zip
git archive --format=tar HEAD | gzip > archive.tar.gz

# Find commit that introduced file
git log --follow -- filename

# Count commits
git rev-list --count branch-name

# File history
git log --follow -p -- filename
```

## Git Flow (Common Workflow)

```bash
# Start new feature
git checkout -b feature/new-feature

# Work on feature
git add .
git commit -m "Implement new feature"

# Keep updated with main
git fetch origin
git rebase origin/main

# Push feature branch
git push -u origin feature/new-feature

# After PR is merged
git checkout main
git pull origin main
git branch -d feature/new-feature

# Clean up remote branch (if not auto-deleted)
git push origin --delete feature/new-feature
```

## Quick Reference

```bash
# Essential commands
git init                    # Initialize repo
git clone <url>             # Clone repo
git status                  # Check status
git add .                   # Stage all changes
git commit -m "message"     # Commit changes
git push                    # Push to remote
git pull                    # Pull from remote
git branch                  # List branches
git checkout -b branch      # Create and switch branch
git merge branch            # Merge branch
git log --oneline           # View history
```

## Tips & Best Practices

1. **Commit Often:** Small, logical commits are better
2. **Write Good Messages:** Use imperative mood ("Add feature" not "Added feature")
3. **Pull Before Push:** Always pull latest changes before pushing
4. **Branch for Features:** Create new branch for each feature
5. **Don't Commit Secrets:** Never commit passwords, API keys, etc.
6. **Use .gitignore:** Ignore build artifacts, dependencies, IDE files
7. **Review Before Commit:** Use `git diff` to review changes
8. **Learn to Rebase:** Keeps history clean
9. **Use SSH Keys:** More secure than HTTPS
10. **Backup Important Work:** Push regularly to remote

## Emergency Commands

```bash
# Oh no, I committed to the wrong branch!
git checkout correct-branch
git cherry-pick <commit-hash>
git checkout wrong-branch
git reset --hard HEAD~1

# I need to undo my last commit but keep changes
git reset --soft HEAD~1

# I accidentally deleted uncommitted changes
git reflog
git checkout -b recovery-branch <commit-hash>

# I pushed something I shouldn't have
git revert <commit-hash>
git push

# Or if no one else pulled yet (DANGEROUS!)
git reset --hard <good-commit-hash>
git push --force-with-lease
```

---

**Remember:**
- `git status` is your friend - use it often!
- When in doubt, create a new branch
- Read error messages carefully
- Use `git help <command>` for detailed help
