# Agile Methodology

## Overview
Agile is a modern approach to software development that emphasizes iterative development, collaboration, and flexibility. This module covers Agile principles, Scrum framework, and practices essential for working in Agile teams.

## Learning Objectives
By the end of this module, you will be able to:
- Understand Agile principles and manifesto
- Apply the Scrum framework
- Participate effectively in Agile ceremonies
- Estimate and plan using Agile techniques
- Use Agile tools and artifacts

## Topics Covered

### 1. Introduction to Agile
- What is Agile?
- Agile Manifesto and principles
- Agile vs Waterfall
- Benefits of Agile methodology
- When to use Agile

### 2. Agile Process
- Iterative and incremental development
- Sprint cycle
- Continuous improvement
- Adaptive planning
- Customer collaboration

### 3. Scrum Framework
- Scrum overview and benefits
- Scrum theory (transparency, inspection, adaptation)
- Empirical process control

### 4. Scrum Roles, Artifacts, Ceremonies
**Roles:**
- Product Owner
- Scrum Master
- Development Team

**Artifacts:**
- Product Backlog
- Sprint Backlog
- Increment
- Definition of Done

**Ceremonies:**
- Sprint Planning
- Daily Standup/Scrum
- Sprint Review
- Sprint Retrospective
- Backlog Refinement

### 5. Agile User Stories
- What are user stories?
- User story format: "As a..., I want..., So that..."
- Acceptance criteria
- INVEST criteria (Independent, Negotiable, Valuable, Estimable, Small, Testable)
- Story splitting techniques

### 6. Agile Estimation and Planning
- Story points vs hours
- Planning Poker
- T-shirt sizing
- Relative estimation
- Velocity tracking
- Release planning

### 7. Sprint Points, Planning Poker, Velocity
- Understanding story points
- Planning Poker process
- Calculating team velocity
- Using velocity for planning
- Commitment vs forecast

### 8. Burndown and Burnup Charts
- Purpose of burndown charts
- Reading and interpreting burndown charts
- Burnup charts
- Tracking progress
- Identifying issues early

### 9. Project Boards
- Kanban boards
- Sprint boards
- Workflow columns
- Work-in-progress (WIP) limits
- Visual management

### 10. Agile Tools
- Jira
- Trello
- Azure DevOps
- Asana
- Monday.com

## Agile Manifesto

### Four Values
1. **Individuals and interactions** over processes and tools
2. **Working software** over comprehensive documentation
3. **Customer collaboration** over contract negotiation
4. **Responding to change** over following a plan

### Twelve Principles
1. Customer satisfaction through early and continuous delivery
2. Welcome changing requirements
3. Deliver working software frequently
4. Business and developers work together daily
5. Build projects around motivated individuals
6. Face-to-face conversation is best
7. Working software is the primary measure of progress
8. Sustainable development pace
9. Technical excellence and good design
10. Simplicity is essential
11. Self-organizing teams
12. Regular reflection and adjustment

## Scrum Events in Detail

### Sprint Planning (2-4 hours for 2-week sprint)
- What can be delivered in the sprint?
- How will the work be achieved?
- Select items from Product Backlog
- Create Sprint Goal
- Break down into tasks

### Daily Standup (15 minutes)
Three questions:
1. What did I do yesterday?
2. What will I do today?
3. Are there any blockers?

### Sprint Review (1-2 hours for 2-week sprint)
- Demo completed work
- Gather feedback
- Update Product Backlog
- Discuss what's next

### Sprint Retrospective (1-1.5 hours for 2-week sprint)
- What went well?
- What didn't go well?
- What can we improve?
- Action items for next sprint

## Writing User Stories

### Format
```
As a [type of user],
I want [an action],
So that [a benefit/value].
```

### Examples

**Good User Story:**
```
As a customer,
I want to filter products by price range,
So that I can find items within my budget.

Acceptance Criteria:
- Min and max price inputs are available
- Filter applies when "Apply" button is clicked
- Results update dynamically
- Price range is displayed in the UI
```

**Another Example:**
```
As a registered user,
I want to reset my password,
So that I can regain access to my account if I forget it.

Acceptance Criteria:
- "Forgot Password" link available on login page
- User receives email with reset link
- Link expires after 24 hours
- New password meets security requirements
```

## Estimation Techniques

### Planning Poker Process
1. Product Owner presents user story
2. Team discusses and asks questions
3. Each member selects a card (Fibonacci: 1, 2, 3, 5, 8, 13, 21)
4. All reveal cards simultaneously
5. Discuss differences, especially outliers
6. Re-estimate until consensus

### Story Point Scale
- **1 point**: Trivial change, <1 hour
- **2 points**: Simple task, few hours
- **3 points**: Moderate complexity, half day
- **5 points**: Average story, 1 day
- **8 points**: Complex, 2-3 days
- **13 points**: Very complex, needs splitting
- **21+ points**: Too large, must split

## Exercises

### Exercise 1: Writing User Stories
Write user stories for the following scenarios:
1. A user wants to add items to a shopping cart
2. An admin needs to generate sales reports
3. A customer wants to track their order status

Include acceptance criteria for each story.

### Exercise 2: Story Point Estimation
Given the following features, estimate story points:
1. Add a logout button to the navigation bar
2. Implement user registration with email verification
3. Create a dashboard with 5 different charts
4. Fix a typo in the about page
5. Integrate third-party payment gateway

### Exercise 3: Create a Sprint Board
1. Draw or create a digital Kanban board with columns:
   - To Do
   - In Progress
   - Code Review
   - Testing
   - Done
2. Add 8-10 user stories/tasks to the board
3. Distribute them across columns as if in mid-sprint

### Exercise 4: Sprint Retrospective
Reflect on a recent project (can be from any course or work):
1. List 3 things that went well
2. List 3 things that didn't go well
3. Propose 3 action items for improvement

### Exercise 5: Product Backlog Prioritization
Given a list of 10 features, prioritize them based on:
- Business value
- User impact
- Technical complexity
- Dependencies

Use MoSCoW method: Must have, Should have, Could have, Won't have

## Best Practices

### For Development Teams
- Keep daily standups focused and brief
- Be transparent about blockers
- Collaborate and help team members
- Focus on delivering value
- Embrace continuous improvement

### For Product Owners
- Keep backlog groomed and prioritized
- Be available for team questions
- Accept or reject work based on Definition of Done
- Represent stakeholder interests
- Provide clear acceptance criteria

### For Scrum Masters
- Facilitate ceremonies effectively
- Remove impediments
- Shield team from distractions
- Coach team on Agile practices
- Foster self-organization

## Common Anti-Patterns to Avoid

- Using daily standup for status reporting to management
- Skipping retrospectives
- Changing sprint scope mid-sprint
- Not defining clear acceptance criteria
- Carrying over incomplete work sprint after sprint
- Having sprints longer than 4 weeks
- Micromanaging the development team

## Additional Resources

### Documentation
- [Scrum Guide](https://scrumguides.org/)
- [Agile Manifesto](https://agilemanifesto.org/)
- [Scrum.org Resources](https://www.scrum.org/resources)

### Books
- "Scrum: The Art of Doing Twice the Work in Half the Time" by Jeff Sutherland
- "User Stories Applied" by Mike Cohn
- "Agile Estimating and Planning" by Mike Cohn

### Tools
- [Jira](https://www.atlassian.com/software/jira)
- [Trello](https://trello.com/)
- [Azure DevOps](https://azure.microsoft.com/en-us/services/devops/)

## Assessment

You should be comfortable with:
- [ ] Explaining Agile principles and benefits
- [ ] Understanding Scrum roles and responsibilities
- [ ] Participating in all Scrum ceremonies
- [ ] Writing effective user stories
- [ ] Estimating work using story points
- [ ] Working with Agile tools and boards
- [ ] Applying Agile practices to projects

## Next Steps

After completing this module, proceed to:
- [Module 4: HTML5](../04-html/)

---

**Time Estimate:** 1 day
**Difficulty:** Beginner
**Prerequisites:** None
