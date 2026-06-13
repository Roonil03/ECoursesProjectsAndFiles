# Architecture and System Deep Dive

## System Architecture and State Flow
The application follows a strictly modular React architecture separating the UI views from game logic and state management.
- **`useTicTacToe.js` Hook:** Centralizes the state machine of the game. It controls transitions between the 'intro' phase and 'playing' phase, and manages the board array, turn tracking (`isXNext`), and win resolution (`winner`, `winningLine`).
- **Component Isolation:** The root `App.jsx` simply orchestrates rendering the configuration `IntroScreen` or the active `GameBoard` and `GameControls`.

## Scalable N-by-N Win Engine
Located in `utils/gameLogic.js`, the `calculateWinner` algorithm avoids hardcoded winning combinations. Instead, it dynamically checks:
- $N$ horizontal rows
- $N$ vertical columns
- The 2 main diagonals
It operates in $O(N^2)$ to evaluate the full board, scaling dynamically to any $N \times N$ matrix size without hardcoded indices.

## AI Bot Strategy
The `aiEngine.js` features two distinct levels of difficulty to ensure zero backend dependencies:
1. **Easy Mode:** Analyzes available slots and selects one at random using `Math.random()`.
2. **Hard Mode (Minimax with Depth Limits and Alpha-Beta Pruning):**
   - For **3x3**: Runs a full Minimax algorithm. The game tree is small enough to navigate entirely, resulting in absolute perfect play.
   - For **4x4 and 5x5**: The state space for larger matrices is astronomically high, causing standard Minimax to freeze the browser's UI thread. We implement **Alpha-Beta Pruning** combined with **Depth-Limiting** (e.g. max depth of 4-5 based on remaining moves).
   - **Heuristic Evaluation:** When max depth is reached, the leaf nodes are assigned a heuristic score rather than absolute win/loss points. The board is evaluated for "open lines" of X's or O's, assigning exponentially higher scores (e.g., $10^2$, $10^3$) for pieces positioned to win on subsequent turns, while effectively blocking the opponent's lines. 

## Installation & Setup
To run this application locally:

1. Ensure you have Node.js installed.
2. Install the dependencies:
   ```bash
   npm install
   ```
3. Start the Vite development server:
   ```bash
   npm run dev
   ```
4. Open your browser to the local address provided (typically `http://localhost:5173`).
