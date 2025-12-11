# Exercise: Build an Interactive Quiz Application

## Objective
Create an interactive quiz application using jQuery that demonstrates DOM manipulation, event handling, animations, and AJAX.

## Requirements

Build a quiz application (`quiz.html` and `quiz.js`) with the following features:

### 1. Quiz Structure

#### Question Display
- Show one question at a time
- Multiple choice options (4 options per question)
- Progress indicator (Question 3 of 10)
- Timer for each question (30 seconds)

#### Navigation
- Next/Previous buttons
- Question number indicators (clickable)
- Submit button on last question

#### Results Screen
- Show score (e.g., 7/10 correct)
- Percentage score
- Review each question with correct/incorrect indicators
- Restart quiz button

### 2. jQuery Features to Demonstrate

#### DOM Manipulation
- `.html()`, `.text()`, `.val()`
- `.addClass()`, `.removeClass()`, `.toggleClass()`
- `.append()`, `.prepend()`, `.remove()`
- `.attr()`, `.data()`

#### Events
- `.on()`, `.off()`
- `.click()`, `.change()`
- Event delegation

#### Effects & Animations
- `.fadeIn()`, `.fadeOut()`
- `.slideUp()`, `.slideDown()`
- `.animate()` for custom animations

#### AJAX (Simulated)
- `.ajax()` or `$.getJSON()` to load questions
- Loading indicator while fetching
- Error handling

### 3. Quiz Data Structure
```javascript
const quizData = [
    {
        id: 1,
        question: "What does HTML stand for?",
        options: [
            "Hyper Text Markup Language",
            "High Tech Modern Language",
            "Hyper Transfer Markup Language",
            "Home Tool Markup Language"
        ],
        correctAnswer: 0
    },
    // ... more questions
];
```

### 4. Visual Features
- Highlight selected answer
- Show correct/incorrect feedback with colors
- Smooth transitions between questions
- Animated progress bar
- Pulse animation on timer when < 10 seconds

## Skills Tested
- jQuery selectors and traversal
- Event handling with jQuery
- jQuery animations and effects
- Working with JSON data
- Timer implementation
- State management

## Bonus Challenges
1. Add difficulty levels (Easy, Medium, Hard)
2. Add a leaderboard using localStorage
3. Add sound effects for correct/incorrect answers
4. Implement question shuffling
