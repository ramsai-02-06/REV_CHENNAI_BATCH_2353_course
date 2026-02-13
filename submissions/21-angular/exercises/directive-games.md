# Directive Mini-Games

Practice Angular directives through building interactive mini-games. These exercises focus on `*ngFor`, `*ngIf`, `ngClass`, and `ngStyle`.

---

## Game 1: Memory Match

A classic card matching game where players flip cards to find pairs.

### Requirements

#### Functional Requirements
| ID | Requirement |
|----|-------------|
| F1 | Display a 4x4 grid of face-down cards (8 pairs) |
| F2 | Click a card to flip it face-up |
| F3 | Allow only 2 cards to be flipped at a time |
| F4 | If cards match, keep them face-up (matched state) |
| F5 | If cards don't match, flip both back after 1 second |
| F6 | Track and display number of moves (attempts) |
| F7 | Display "You Win!" message when all pairs are found |
| F8 | Provide a "New Game" button to reset and shuffle |

#### Directive Usage Requirements
| Directive | Usage |
|-----------|-------|
| `*ngFor` | Render the grid of cards |
| `*ngIf` | Show/hide win message, show card content when flipped |
| `ngClass` | Apply `.flipped`, `.matched`, `.disabled` classes |
| `ngStyle` | Apply card background colors or images |

### Screen Design

```
┌─────────────────────────────────────────────────────────┐
│                    MEMORY MATCH                         │
│                    Moves: 12                            │
├─────────────────────────────────────────────────────────┤
│                                                         │
│    ┌─────┐  ┌─────┐  ┌─────┐  ┌─────┐                  │
│    │  ?  │  │  ?  │  │ CAT │  │ CAT │   <- matched     │
│    │     │  │     │  │ 🐱  │  │ 🐱  │      (stay up)   │
│    └─────┘  └─────┘  └─────┘  └─────┘                  │
│                                                         │
│    ┌─────┐  ┌─────┐  ┌─────┐  ┌─────┐                  │
│    │  ?  │  │ DOG │  │  ?  │  │  ?  │   <- DOG flipped │
│    │     │  │ 🐕  │  │     │  │     │                  │
│    └─────┘  └─────┘  └─────┘  └─────┘                  │
│                                                         │
│    ┌─────┐  ┌─────┐  ┌─────┐  ┌─────┐                  │
│    │  ?  │  │  ?  │  │  ?  │  │  ?  │                  │
│    │     │  │     │  │     │  │     │                  │
│    └─────┘  └─────┘  └─────┘  └─────┘                  │
│                                                         │
│    ┌─────┐  ┌─────┐  ┌─────┐  ┌─────┐                  │
│    │  ?  │  │  ?  │  │  ?  │  │  ?  │                  │
│    │     │  │     │  │     │  │     │                  │
│    └─────┘  └─────┘  └─────┘  └─────┘                  │
│                                                         │
│                   [ New Game ]                          │
└─────────────────────────────────────────────────────────┘

Win State:
┌─────────────────────────────────────────────────────────┐
│                                                         │
│              ★ ★ ★  YOU WIN!  ★ ★ ★                    │
│                                                         │
│              Completed in 18 moves!                     │
│                                                         │
│                   [ Play Again ]                        │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

### Card States

```
Face Down:           Face Up:              Matched:
┌─────────────┐     ┌─────────────┐       ┌─────────────┐
│░░░░░░░░░░░░░│     │             │       │             │
│░░░░░ ? ░░░░░│     │   APPLE     │       │   APPLE     │
│░░░░░░░░░░░░░│     │    🍎       │       │    🍎       │
│░░░░░░░░░░░░░│     │             │       │             │
└─────────────┘     └─────────────┘       └─────────────┘
 (clickable)         (revealing)           (green border)
```

### Data Model

```typescript
interface Card {
  id: number;
  value: string;      // e.g., 'apple', 'banana'
  emoji: string;      // e.g., '🍎', '🍌'
  isFlipped: boolean;
  isMatched: boolean;
}

// Component properties
cards: Card[] = [];
flippedCards: Card[] = [];
moves: number = 0;
isGameWon: boolean = false;
isProcessing: boolean = false;  // Prevent clicks during flip animation
```

### Implementation Steps

1. **Create the component**
   ```bash
   ng g c games/memory-match
   ```

2. **Initialize cards** (shuffle pairs)
   ```typescript
   cardValues = [
     { value: 'apple', emoji: '🍎' },
     { value: 'banana', emoji: '🍌' },
     { value: 'cherry', emoji: '🍒' },
     { value: 'grape', emoji: '🍇' },
     { value: 'orange', emoji: '🍊' },
     { value: 'lemon', emoji: '🍋' },
     { value: 'peach', emoji: '🍑' },
     { value: 'melon', emoji: '🍈' },
   ];

   initGame(): void {
     // Create pairs, shuffle, assign IDs
   }
   ```

3. **Template structure**
   ```html
   <div class="game-container">
     <h1>Memory Match</h1>
     <p>Moves: {{ moves }}</p>

     <div class="card-grid">
       <div *ngFor="let card of cards; trackBy: trackByCardId"
            class="card"
            [ngClass]="{
              'flipped': card.isFlipped,
              'matched': card.isMatched,
              'disabled': isProcessing || card.isMatched
            }"
            (click)="flipCard(card)">

         <div class="card-inner">
           <div class="card-front">?</div>
           <div class="card-back">
             <span class="emoji">{{ card.emoji }}</span>
             <span class="label">{{ card.value | uppercase }}</span>
           </div>
         </div>
       </div>
     </div>

     <div *ngIf="isGameWon" class="win-overlay">
       <h2>You Win!</h2>
       <p>Completed in {{ moves }} moves!</p>
       <button (click)="initGame()">Play Again</button>
     </div>

     <button (click)="initGame()">New Game</button>
   </div>
   ```

4. **Flip logic**
   ```typescript
   flipCard(card: Card): void {
     if (this.isProcessing || card.isFlipped || card.isMatched) return;

     card.isFlipped = true;
     this.flippedCards.push(card);

     if (this.flippedCards.length === 2) {
       this.moves++;
       this.checkMatch();
     }
   }

   checkMatch(): void {
     this.isProcessing = true;
     const [card1, card2] = this.flippedCards;

     if (card1.value === card2.value) {
       card1.isMatched = true;
       card2.isMatched = true;
       this.flippedCards = [];
       this.isProcessing = false;
       this.checkWin();
     } else {
       setTimeout(() => {
         card1.isFlipped = false;
         card2.isFlipped = false;
         this.flippedCards = [];
         this.isProcessing = false;
       }, 1000);
     }
   }
   ```

### CSS Requirements

```css
.card-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
  max-width: 400px;
  margin: 0 auto;
}

.card {
  aspect-ratio: 1;
  cursor: pointer;
  perspective: 1000px;
}

.card-inner {
  width: 100%;
  height: 100%;
  transition: transform 0.5s;
  transform-style: preserve-3d;
}

.card.flipped .card-inner {
  transform: rotateY(180deg);
}

.card-front, .card-back {
  position: absolute;
  width: 100%;
  height: 100%;
  backface-visibility: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
}

.card-front {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 2rem;
}

.card-back {
  background: white;
  transform: rotateY(180deg);
  flex-direction: column;
  border: 2px solid #ddd;
}

.card.matched .card-back {
  border-color: #4caf50;
  background: #e8f5e9;
}

.card.disabled {
  pointer-events: none;
}

.win-overlay {
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.8);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: white;
}
```

### Submission Checklist

- [ ] 4x4 grid renders with `*ngFor`
- [ ] Cards flip on click with `ngClass` animation
- [ ] Only 2 cards flip at a time
- [ ] Matched pairs stay revealed
- [ ] Non-matches flip back after delay
- [ ] Move counter increments correctly
- [ ] Win message appears with `*ngIf`
- [ ] New Game button shuffles and resets

---

## Game 2: Color Sequence (Simon Says)

A memory game where players must repeat an increasingly long sequence of colors.

### Requirements

#### Functional Requirements
| ID | Requirement |
|----|-------------|
| F1 | Display 4 colored buttons (red, green, blue, yellow) |
| F2 | Game shows a sequence by lighting up buttons |
| F3 | Player must click buttons in the same order |
| F4 | Each round adds one more color to sequence |
| F5 | Show current round/level number |
| F6 | Flash button brighter when it's "playing" in sequence |
| F7 | Flash button when player clicks it |
| F8 | Show "Game Over" if player clicks wrong color |
| F9 | Track and display high score (longest sequence) |
| F10 | "Start Game" button to begin |

#### Directive Usage Requirements
| Directive | Usage |
|-----------|-------|
| `*ngFor` | Render the 4 color buttons |
| `*ngIf` | Show/hide start button, game over, score display |
| `ngClass` | Apply `.active`, `.playing`, `.player-turn` classes |
| `ngStyle` | Dynamic brightness/opacity when lit |

### Screen Design

```
┌─────────────────────────────────────────────────────────┐
│                   COLOR SEQUENCE                        │
│             Round: 5    High Score: 8                   │
├─────────────────────────────────────────────────────────┤
│                                                         │
│                  ┌───────────────┐                      │
│                  │               │                      │
│                  │     GREEN     │   <- currently lit   │
│                  │    (bright)   │                      │
│                  │               │                      │
│       ┌──────────┼───────────────┼──────────┐          │
│       │          │               │          │          │
│       │   RED    │               │   BLUE   │          │
│       │  (dim)   │               │  (dim)   │          │
│       │          │               │          │          │
│       └──────────┼───────────────┼──────────┘          │
│                  │               │                      │
│                  │    YELLOW     │                      │
│                  │    (dim)      │                      │
│                  │               │                      │
│                  └───────────────┘                      │
│                                                         │
│              Status: Watch the sequence...              │
│                                                         │
└─────────────────────────────────────────────────────────┘

Game Over State:
┌─────────────────────────────────────────────────────────┐
│                                                         │
│                     GAME OVER!                          │
│                                                         │
│                 You reached Round 5                     │
│                                                         │
│                 Your sequence: 🔴🟢🔵🟢🟡               │
│               Correct sequence: 🔴🟢🔵🟢🔴              │
│                                  ↑ wrong                │
│                                                         │
│                    [ Try Again ]                        │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

### Button States

```
Normal (dim):           Active (lit):            Player clicked:
┌─────────────────┐    ┌─────────────────┐      ┌─────────────────┐
│                 │    │▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓│      │░░░░░░░░░░░░░░░░░│
│     GREEN       │    │▓▓▓  GREEN   ▓▓▓│      │░░░  GREEN   ░░░│
│  opacity: 0.4   │    │▓▓▓  BRIGHT  ▓▓▓│      │░░░  FLASH   ░░░│
│                 │    │▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓│      │░░░░░░░░░░░░░░░░░│
└─────────────────┘    └─────────────────┘      └─────────────────┘
```

### Data Model

```typescript
interface ColorButton {
  id: number;
  name: string;
  color: string;
  activeColor: string;  // Brighter version
}

// Component properties
buttons: ColorButton[] = [
  { id: 0, name: 'green', color: '#2e7d32', activeColor: '#4caf50' },
  { id: 1, name: 'red', color: '#c62828', activeColor: '#f44336' },
  { id: 2, name: 'blue', color: '#1565c0', activeColor: '#2196f3' },
  { id: 3, name: 'yellow', color: '#f9a825', activeColor: '#ffeb3b' },
];

sequence: number[] = [];         // Computer's sequence
playerSequence: number[] = [];   // Player's input
round: number = 0;
highScore: number = 0;
isPlaying: boolean = false;      // Sequence is playing
isPlayerTurn: boolean = false;   // Player can click
activeButton: number | null = null;
gameOver: boolean = false;
gameStarted: boolean = false;
```

### Implementation Steps

1. **Create the component**
   ```bash
   ng g c games/color-sequence
   ```

2. **Template structure**
   ```html
   <div class="game-container">
     <h1>Color Sequence</h1>

     <div class="scores" *ngIf="gameStarted">
       <span>Round: {{ round }}</span>
       <span>High Score: {{ highScore }}</span>
     </div>

     <div class="button-container">
       <button *ngFor="let btn of buttons; trackBy: trackById"
               class="color-btn"
               [ngClass]="{
                 'active': activeButton === btn.id,
                 'disabled': !isPlayerTurn
               }"
               [ngStyle]="{
                 'background-color': activeButton === btn.id ? btn.activeColor : btn.color,
                 'opacity': activeButton === btn.id ? 1 : 0.6,
                 'transform': activeButton === btn.id ? 'scale(1.1)' : 'scale(1)'
               }"
               [attr.data-color]="btn.name"
               (click)="handlePlayerClick(btn.id)"
               [disabled]="!isPlayerTurn">
       </button>
     </div>

     <p class="status">
       <span *ngIf="!gameStarted">Press Start to begin!</span>
       <span *ngIf="isPlaying">Watch the sequence...</span>
       <span *ngIf="isPlayerTurn">Your turn! Repeat the sequence.</span>
     </p>

     <button *ngIf="!gameStarted" (click)="startGame()" class="start-btn">
       Start Game
     </button>

     <div *ngIf="gameOver" class="game-over-overlay">
       <h2>Game Over!</h2>
       <p>You reached Round {{ round }}</p>
       <button (click)="startGame()">Try Again</button>
     </div>
   </div>
   ```

3. **Game logic**
   ```typescript
   startGame(): void {
     this.sequence = [];
     this.playerSequence = [];
     this.round = 0;
     this.gameOver = false;
     this.gameStarted = true;
     this.nextRound();
   }

   nextRound(): void {
     this.round++;
     this.playerSequence = [];
     this.isPlayerTurn = false;

     // Add random color to sequence
     const randomIndex = Math.floor(Math.random() * 4);
     this.sequence.push(randomIndex);

     // Play sequence
     this.playSequence();
   }

   async playSequence(): Promise<void> {
     this.isPlaying = true;

     for (const buttonId of this.sequence) {
       await this.delay(300);
       this.activeButton = buttonId;
       await this.delay(500);
       this.activeButton = null;
     }

     this.isPlaying = false;
     this.isPlayerTurn = true;
   }

   handlePlayerClick(buttonId: number): void {
     if (!this.isPlayerTurn) return;

     // Flash the button
     this.activeButton = buttonId;
     setTimeout(() => this.activeButton = null, 200);

     this.playerSequence.push(buttonId);
     const currentIndex = this.playerSequence.length - 1;

     // Check if correct
     if (this.playerSequence[currentIndex] !== this.sequence[currentIndex]) {
       this.gameOver = true;
       this.isPlayerTurn = false;
       if (this.round > this.highScore) {
         this.highScore = this.round;
       }
       return;
     }

     // Check if sequence complete
     if (this.playerSequence.length === this.sequence.length) {
       this.isPlayerTurn = false;
       setTimeout(() => this.nextRound(), 1000);
     }
   }

   delay(ms: number): Promise<void> {
     return new Promise(resolve => setTimeout(resolve, ms));
   }
   ```

### CSS Requirements

```css
.game-container {
  text-align: center;
  padding: 20px;
}

.button-container {
  display: grid;
  grid-template-areas:
    ". green ."
    "red . blue"
    ". yellow .";
  gap: 10px;
  max-width: 300px;
  margin: 20px auto;
}

.color-btn {
  width: 100px;
  height: 100px;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.15s ease;
}

.color-btn:nth-child(1) { grid-area: green; }
.color-btn:nth-child(2) { grid-area: red; }
.color-btn:nth-child(3) { grid-area: blue; }
.color-btn:nth-child(4) { grid-area: yellow; }

.color-btn.active {
  box-shadow: 0 0 30px currentColor;
}

.color-btn.disabled {
  cursor: not-allowed;
}

.status {
  font-size: 1.2rem;
  margin: 20px 0;
  min-height: 30px;
}

.start-btn {
  padding: 15px 40px;
  font-size: 1.2rem;
  background: #4caf50;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.game-over-overlay {
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.9);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: white;
}
```

### Submission Checklist

- [ ] 4 color buttons render with `*ngFor`
- [ ] Buttons light up during sequence playback (`ngStyle` opacity/color)
- [ ] `*ngIf` shows correct status message
- [ ] Player can only click during their turn
- [ ] Sequence validates player input correctly
- [ ] Wrong click triggers game over
- [ ] High score persists between games
- [ ] New round adds to sequence

---

## Game 3: Whack-a-Mole

A reaction game where moles pop up randomly and players must click them quickly.

### Requirements

#### Functional Requirements
| ID | Requirement |
|----|-------------|
| F1 | Display a 3x3 grid of mole holes |
| F2 | Moles pop up randomly in holes |
| F3 | Only 1-2 moles visible at a time |
| F4 | Mole stays visible for limited time (decreases as game progresses) |
| F5 | Click a mole to score points (+10) |
| F6 | Miss a mole (disappears before click) loses points (-5) |
| F7 | Game runs for 30 seconds |
| F8 | Display countdown timer |
| F9 | Display current score |
| F10 | Show final score and high score when time ends |

#### Directive Usage Requirements
| Directive | Usage |
|-----------|-------|
| `*ngFor` | Render the 3x3 grid of holes |
| `*ngIf` | Show/hide mole in each hole, timer display, game over |
| `ngClass` | Apply `.mole-visible`, `.mole-hit`, `.hole-empty` classes |
| `ngStyle` | Mole position/animation, timer color (red when low) |

### Screen Design

```
┌─────────────────────────────────────────────────────────┐
│                    WHACK-A-MOLE                         │
│            Score: 120        Time: 23s                  │
├─────────────────────────────────────────────────────────┤
│                                                         │
│    ┌─────────┐    ┌─────────┐    ┌─────────┐           │
│    │  ___    │    │         │    │  ___    │           │
│    │ (o o)   │    │   ~~~   │    │ (o o)   │   <- mole │
│    │  \_/    │    │  hole   │    │  \_/    │           │
│    │ /███\   │    │         │    │ /███\   │           │
│    └─────────┘    └─────────┘    └─────────┘           │
│                                                         │
│    ┌─────────┐    ┌─────────┐    ┌─────────┐           │
│    │         │    │         │    │         │           │
│    │   ~~~   │    │   ~~~   │    │   ~~~   │           │
│    │  hole   │    │  hole   │    │  hole   │           │
│    │         │    │         │    │         │           │
│    └─────────┘    └─────────┘    └─────────┘           │
│                                                         │
│    ┌─────────┐    ┌─────────┐    ┌─────────┐           │
│    │         │    │  ___    │    │         │           │
│    │   ~~~   │    │ (x x)   │    │   ~~~   │   <- hit! │
│    │  hole   │    │  \_/    │    │  hole   │           │
│    │         │    │ /███\   │    │         │           │
│    └─────────┘    └─────────┘    └─────────┘           │
│                                                         │
│                    [ Start Game ]                       │
└─────────────────────────────────────────────────────────┘

Game Over State:
┌─────────────────────────────────────────────────────────┐
│                                                         │
│                    TIME'S UP!                           │
│                                                         │
│                 Final Score: 180                        │
│                 High Score:  240                        │
│                                                         │
│                 Moles Hit: 20                           │
│                 Moles Missed: 4                         │
│                 Accuracy: 83%                           │
│                                                         │
│                    [ Play Again ]                       │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

### Hole/Mole States

```
Empty Hole:              Mole Visible:            Mole Hit:
┌─────────────────┐     ┌─────────────────┐      ┌─────────────────┐
│                 │     │      ___        │      │      ___        │
│    ~~~~~~~~~    │     │     (o o)       │      │     (x x)       │
│    ~~~~~~~~~    │     │      \_/        │      │      \_/        │
│   (dark hole)   │     │     /███\       │      │     /███\       │
└─────────────────┘     └─────────────────┘      └─────────────────┘
                        animation: pop up         animation: bonk!
                        cursor: pointer           +10 floats up
```

### Data Model

```typescript
interface Hole {
  id: number;
  hasMole: boolean;
  isHit: boolean;
}

// Component properties
holes: Hole[] = [];
score: number = 0;
timeLeft: number = 30;
gameRunning: boolean = false;
gameOver: boolean = false;
highScore: number = 0;
molesHit: number = 0;
molesMissed: number = 0;

moleVisibleTime: number = 1200;  // ms - decreases over time
spawnInterval: number = 1000;     // ms - decreases over time
```

### Implementation Steps

1. **Create the component**
   ```bash
   ng g c games/whack-a-mole
   ```

2. **Initialize grid**
   ```typescript
   ngOnInit(): void {
     this.initHoles();
   }

   initHoles(): void {
     this.holes = Array.from({ length: 9 }, (_, i) => ({
       id: i,
       hasMole: false,
       isHit: false
     }));
   }
   ```

3. **Template structure**
   ```html
   <div class="game-container">
     <h1>Whack-a-Mole</h1>

     <div class="game-stats" *ngIf="gameRunning || gameOver">
       <span class="score">Score: {{ score }}</span>
       <span class="timer"
             [ngStyle]="{ 'color': timeLeft <= 10 ? '#f44336' : '#333' }"
             [ngClass]="{ 'pulse': timeLeft <= 5 }">
         Time: {{ timeLeft }}s
       </span>
     </div>

     <div class="game-grid">
       <div *ngFor="let hole of holes; trackBy: trackByHoleId"
            class="hole"
            [ngClass]="{
              'has-mole': hole.hasMole,
              'mole-hit': hole.isHit
            }"
            (click)="whackHole(hole)">

         <div class="hole-dirt"></div>

         <div *ngIf="hole.hasMole"
              class="mole"
              [ngClass]="{ 'hit': hole.isHit }">
           <span class="mole-face">{{ hole.isHit ? '(x x)' : '(o o)' }}</span>
           <span class="mole-body">/███\</span>
         </div>

         <div *ngIf="hole.isHit" class="score-popup">+10</div>
       </div>
     </div>

     <button *ngIf="!gameRunning && !gameOver"
             (click)="startGame()"
             class="start-btn">
       Start Game
     </button>

     <div *ngIf="gameOver" class="game-over">
       <h2>Time's Up!</h2>
       <p>Final Score: {{ score }}</p>
       <p>High Score: {{ highScore }}</p>
       <p>Moles Hit: {{ molesHit }} | Missed: {{ molesMissed }}</p>
       <p>Accuracy: {{ getAccuracy() }}%</p>
       <button (click)="startGame()">Play Again</button>
     </div>
   </div>
   ```

4. **Game logic**
   ```typescript
   startGame(): void {
     this.score = 0;
     this.timeLeft = 30;
     this.molesHit = 0;
     this.molesMissed = 0;
     this.gameOver = false;
     this.gameRunning = true;
     this.moleVisibleTime = 1200;
     this.initHoles();

     this.startTimer();
     this.spawnMoles();
   }

   startTimer(): void {
     const timer = setInterval(() => {
       this.timeLeft--;

       // Increase difficulty over time
       if (this.timeLeft % 10 === 0 && this.moleVisibleTime > 600) {
         this.moleVisibleTime -= 100;
       }

       if (this.timeLeft <= 0) {
         clearInterval(timer);
         this.endGame();
       }
     }, 1000);
   }

   spawnMoles(): void {
     if (!this.gameRunning) return;

     // Find empty holes
     const emptyHoles = this.holes.filter(h => !h.hasMole);
     if (emptyHoles.length === 0) {
       setTimeout(() => this.spawnMoles(), 200);
       return;
     }

     // Pick random empty hole
     const randomHole = emptyHoles[Math.floor(Math.random() * emptyHoles.length)];
     randomHole.hasMole = true;
     randomHole.isHit = false;

     // Hide mole after timeout (if not hit)
     setTimeout(() => {
       if (randomHole.hasMole && !randomHole.isHit) {
         randomHole.hasMole = false;
         this.molesMissed++;
         this.score = Math.max(0, this.score - 5);
       }
     }, this.moleVisibleTime);

     // Spawn next mole
     const nextSpawn = 800 + Math.random() * 600;
     setTimeout(() => this.spawnMoles(), nextSpawn);
   }

   whackHole(hole: Hole): void {
     if (!this.gameRunning || !hole.hasMole || hole.isHit) return;

     hole.isHit = true;
     this.score += 10;
     this.molesHit++;

     // Hide after short delay
     setTimeout(() => {
       hole.hasMole = false;
       hole.isHit = false;
     }, 300);
   }

   endGame(): void {
     this.gameRunning = false;
     this.gameOver = true;
     this.holes.forEach(h => h.hasMole = false);

     if (this.score > this.highScore) {
       this.highScore = this.score;
     }
   }

   getAccuracy(): number {
     const total = this.molesHit + this.molesMissed;
     return total === 0 ? 0 : Math.round((this.molesHit / total) * 100);
   }
   ```

### CSS Requirements

```css
.game-container {
  text-align: center;
  padding: 20px;
  background: #8b4513;
  min-height: 100vh;
}

h1 {
  color: #fff;
  text-shadow: 2px 2px 4px rgba(0,0,0,0.5);
}

.game-stats {
  display: flex;
  justify-content: center;
  gap: 40px;
  font-size: 1.5rem;
  color: white;
  margin: 20px 0;
}

.timer.pulse {
  animation: pulse 0.5s infinite;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.1); }
}

.game-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 15px;
  max-width: 360px;
  margin: 0 auto;
}

.hole {
  width: 100px;
  height: 100px;
  background: #654321;
  border-radius: 50%;
  position: relative;
  cursor: pointer;
  overflow: hidden;
}

.hole-dirt {
  position: absolute;
  bottom: 0;
  width: 100%;
  height: 40%;
  background: #3d2914;
  border-radius: 0 0 50% 50%;
}

.mole {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  animation: popUp 0.15s ease-out;
  display: flex;
  flex-direction: column;
  align-items: center;
  font-family: monospace;
  color: #333;
  background: #8b7355;
  padding: 5px;
  border-radius: 50% 50% 0 0;
}

.mole.hit {
  animation: bonk 0.2s ease-out;
}

@keyframes popUp {
  from { transform: translateX(-50%) translateY(100%); }
  to { transform: translateX(-50%) translateY(0); }
}

@keyframes bonk {
  0% { transform: translateX(-50%) scale(1); }
  50% { transform: translateX(-50%) scale(0.8); }
  100% { transform: translateX(-50%) scale(1); }
}

.score-popup {
  position: absolute;
  top: 10px;
  left: 50%;
  transform: translateX(-50%);
  color: #4caf50;
  font-weight: bold;
  font-size: 1.5rem;
  animation: floatUp 0.5s ease-out forwards;
}

@keyframes floatUp {
  to {
    transform: translateX(-50%) translateY(-30px);
    opacity: 0;
  }
}

.start-btn {
  padding: 15px 40px;
  font-size: 1.2rem;
  background: #4caf50;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  margin-top: 20px;
}

.game-over {
  background: white;
  padding: 30px;
  border-radius: 16px;
  max-width: 300px;
  margin: 20px auto;
}
```

### Submission Checklist

- [ ] 3x3 grid renders with `*ngFor`
- [ ] Moles appear randomly with `*ngIf`
- [ ] Mole pop-up animation works
- [ ] Click detection works on moles only
- [ ] Score increases on hit (+10)
- [ ] Score decreases on miss (-5)
- [ ] Timer counts down and changes color with `ngStyle`
- [ ] Game ends after 30 seconds
- [ ] Final stats display with `*ngIf`
- [ ] Difficulty increases over time

---

## General Submission Guidelines

### File Structure
```
task-tracker/
├── src/app/
│   ├── games/
│   │   ├── memory-match/
│   │   │   ├── memory-match.component.ts
│   │   │   ├── memory-match.component.html
│   │   │   └── memory-match.component.css
│   │   ├── color-sequence/
│   │   │   ├── color-sequence.component.ts
│   │   │   ├── color-sequence.component.html
│   │   │   └── color-sequence.component.css
│   │   └── whack-a-mole/
│   │       ├── whack-a-mole.component.ts
│   │       ├── whack-a-mole.component.html
│   │       └── whack-a-mole.component.css
```

### Evaluation Criteria

| Criteria | Points |
|----------|--------|
| Correct use of `*ngFor` with trackBy | 10 |
| Correct use of `*ngIf` / `*ngIf; else` | 10 |
| Correct use of `ngClass` for state | 10 |
| Correct use of `ngStyle` for dynamic styles | 10 |
| Game logic works correctly | 20 |
| UI matches screen designs | 15 |
| Animations are smooth | 10 |
| Code is clean and organized | 10 |
| Edge cases handled | 5 |
| **Total** | **100** |

### Bonus Points

- Add sound effects (+5)
- Add difficulty selector (easy/medium/hard) (+5)
- Make responsive for mobile (+5)
- Add local storage for high scores (+5)
- Create custom directive for animations (+10)
