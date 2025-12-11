# Exercise: Git Branch Management Simulation

## Objective
Practice essential Git branching workflows including creating branches, making commits, merging, and resolving conflicts.

## Scenario
You're working on a team project. You need to implement a new feature while another developer has made changes to the same file on the main branch.

## Tasks

### Part 1: Setup Repository
1. Create a new directory called `git-exercise`
2. Initialize a Git repository
3. Create a file called `calculator.py` with basic add and subtract functions
4. Commit this initial version

### Part 2: Feature Branch Workflow
1. Create and switch to a new branch called `feature/multiply`
2. Add a multiply function to `calculator.py`
3. Commit your changes with a meaningful message
4. Switch back to `main` branch
5. Add a divide function to `calculator.py` (simulating another developer's work)
6. Commit this change

### Part 3: Merge and Conflict Resolution
1. Merge `feature/multiply` into `main`
2. Resolve any merge conflicts that arise
3. Complete the merge with a proper commit message

### Part 4: View History
1. Use `git log` to view the commit history with a graph
2. Show the difference between commits

## Expected Final File Structure
```python
# calculator.py
def add(a, b):
    return a + b

def subtract(a, b):
    return a - b

def multiply(a, b):
    return a * b

def divide(a, b):
    if b == 0:
        raise ValueError("Cannot divide by zero")
    return a / b
```

## Skills Tested
- `git init`, `git add`, `git commit`
- `git branch`, `git checkout` / `git switch`
- `git merge`
- Conflict resolution
- `git log --oneline --graph`

## Deliverables
Document the Git commands you used at each step.
