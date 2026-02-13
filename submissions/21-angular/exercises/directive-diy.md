# Directive Exercise: Emoji Clicker (Do It Yourself)

Build this game on your own. Only requirements provided - write the code yourself!

---

## Game Overview

Click on emojis as they appear on the screen. Score points for each click. Game ends after 20 seconds.

---

## Screen Design

### Game Screen

```
┌─────────────────────────────────────────────┐
│            EMOJI CLICKER                    │
│         Score: 15    Time: 12s              │
├─────────────────────────────────────────────┤
│                                             │
│    ┌─────┐   ┌─────┐   ┌─────┐             │
│    │     │   │ 🎯  │   │     │             │
│    │     │   │     │   │     │             │
│    └─────┘   └─────┘   └─────┘             │
│                                             │
│    ┌─────┐   ┌─────┐   ┌─────┐             │
│    │     │   │     │   │ 🎯  │             │
│    │     │   │     │   │     │             │
│    └─────┘   └─────┘   └─────┘             │
│                                             │
│    ┌─────┐   ┌─────┐   ┌─────┐             │
│    │ 🎯  │   │     │   │     │             │
│    │     │   │     │   │     │             │
│    └─────┘   └─────┘   └─────┘             │
│                                             │
└─────────────────────────────────────────────┘
```

### Start Screen

```
┌─────────────────────────────────────────────┐
│                                             │
│            EMOJI CLICKER                    │
│                                             │
│       Click emojis as fast as you can!      │
│                                             │
│              [ START GAME ]                 │
│                                             │
└─────────────────────────────────────────────┘
```

### Game Over Screen

```
┌─────────────────────────────────────────────┐
│                                             │
│              GAME OVER!                     │
│                                             │
│           Your Score: 25                    │
│           High Score: 32                    │
│                                             │
│              [ PLAY AGAIN ]                 │
│                                             │
└─────────────────────────────────────────────┘
```

---

## Requirements

### Functional Requirements

| ID | Requirement |
|----|-------------|
| F1 | Display a 3x3 grid of boxes |
| F2 | Show "Start Game" button before game begins |
| F3 | When game starts, show score and timer |
| F4 | Emoji appears in random box every 1 second |
| F5 | Only 1 emoji visible at a time |
| F6 | Clicking emoji adds 1 point to score |
| F7 | Timer counts down from 20 to 0 |
| F8 | When timer reaches 0, show "Game Over" |
| F9 | Display final score and high score |
| F10 | "Play Again" button restarts the game |

### Directive Requirements

| Directive | Where to Use |
|-----------|--------------|
| `*ngFor` | Render the 3x3 grid of boxes |
| `*ngIf` | Show/hide emoji in each box |
| `*ngIf` | Show start screen OR game screen OR game over screen |
| `ngClass` | Add `.has-emoji` class when box contains emoji |
| `ngClass` | Add `.clicked` class briefly when emoji is clicked |
| `ngStyle` | Change timer color to red when time < 5 seconds |

---

## Data Model

| Property | Type | Description |
|----------|------|-------------|
| `boxes` | `number[]` | Array of 9 items (0-8) for the grid |
| `activeBox` | `number` | Which box has the emoji (-1 = none) |
| `score` | `number` | Current score |
| `timeLeft` | `number` | Seconds remaining |
| `gameState` | `string` | `'start'`, `'playing'`, or `'gameover'` |
| `highScore` | `number` | Best score achieved |

---

## Game Flow

```
┌─────────────┐     click      ┌─────────────┐
│   START     │ ─────────────► │   PLAYING   │
│   SCREEN    │   "Start"      │   SCREEN    │
└─────────────┘                └──────┬──────┘
                                      │
                               timer = 0
                                      │
                                      ▼
┌─────────────┐     click      ┌─────────────┐
│   START     │ ◄───────────── │  GAME OVER  │
│   SCREEN    │  "Play Again"  │   SCREEN    │
└─────────────┘                └─────────────┘
```

---

## Styling Requirements

| Element | Style |
|---------|-------|
| Grid | 3 columns, gap between boxes |
| Box | Square shape, border, centered content |
| Box with emoji | Highlighted background |
| Emoji | Large font size (2rem+) |
| Timer (normal) | Black text |
| Timer (< 5 sec) | Red text, pulsing animation |
| Buttons | Rounded corners, hover effect |

---

## Submission Checklist

- [ ] 3x3 grid displays using `*ngFor`
- [ ] Start screen shows with `*ngIf`
- [ ] Game screen shows with `*ngIf`
- [ ] Emoji appears in random box using `*ngIf`
- [ ] Box highlights with `ngClass` when it has emoji
- [ ] Timer changes color with `ngStyle`
- [ ] Score increments on emoji click
- [ ] Game over shows when timer reaches 0
- [ ] High score is tracked
- [ ] Play again works correctly

---

## Evaluation (50 points)

| Criteria | Points |
|----------|--------|
| `*ngFor` renders grid correctly | 8 |
| `*ngIf` shows correct screen | 8 |
| `*ngIf` shows/hides emoji | 8 |
| `ngClass` highlights active box | 8 |
| `ngStyle` changes timer color | 8 |
| Game logic works correctly | 10 |
| **Total** | **50** |
