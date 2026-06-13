import { calculateWinner } from './gameLogic';

function evaluateLine(line, aiPlayer, humanPlayer) {
  let aiCount = 0;
  let humanCount = 0;
  for (const cell of line) {
    if (cell === aiPlayer) aiCount++;
    else if (cell === humanPlayer) humanCount++;
  }
  
  if (aiCount > 0 && humanCount > 0) return 0; // blocked line
  
  if (aiCount > 0) {
    return Math.pow(10, aiCount);
  }
  if (humanCount > 0) {
    return -Math.pow(10, humanCount); // negative score for human advantage
  }
  return 0;
}

function evaluateBoard(board, size, aiPlayer, humanPlayer) {
  let score = 0;
  
  // Rows
  for (let i = 0; i < size; i++) {
    const line = [];
    for (let j = 0; j < size; j++) line.push(board[i * size + j]);
    score += evaluateLine(line, aiPlayer, humanPlayer);
  }
  
  // Columns
  for (let i = 0; i < size; i++) {
    const line = [];
    for (let j = 0; j < size; j++) line.push(board[j * size + i]);
    score += evaluateLine(line, aiPlayer, humanPlayer);
  }
  
  // Diagonals
  const mainDiag = [];
  const antiDiag = [];
  for (let i = 0; i < size; i++) {
    mainDiag.push(board[i * size + i]);
    antiDiag.push(board[i * size + (size - 1 - i)]);
  }
  score += evaluateLine(mainDiag, aiPlayer, humanPlayer);
  score += evaluateLine(antiDiag, aiPlayer, humanPlayer);
  
  return score;
}

function minimax(board, depth, isMaximizing, alpha, beta, size, aiPlayer, humanPlayer) {
  const result = calculateWinner(board, size);
  if (result) {
    if (result.winner === aiPlayer) return 1000000 + depth;
    if (result.winner === humanPlayer) return -1000000 - depth;
    if (result.winner === 'Draw') return 0;
  }
  
  if (depth === 0) {
    return evaluateBoard(board, size, aiPlayer, humanPlayer);
  }

  if (isMaximizing) {
    let maxEval = -Infinity;
    for (let i = 0; i < board.length; i++) {
      if (board[i] === null) {
        board[i] = aiPlayer;
        const ev = minimax(board, depth - 1, false, alpha, beta, size, aiPlayer, humanPlayer);
        board[i] = null;
        maxEval = Math.max(maxEval, ev);
        alpha = Math.max(alpha, ev);
        if (beta <= alpha) break;
      }
    }
    return maxEval;
  } else {
    let minEval = Infinity;
    for (let i = 0; i < board.length; i++) {
      if (board[i] === null) {
        board[i] = humanPlayer;
        const ev = minimax(board, depth - 1, true, alpha, beta, size, aiPlayer, humanPlayer);
        board[i] = null;
        minEval = Math.min(minEval, ev);
        beta = Math.min(beta, ev);
        if (beta <= alpha) break;
      }
    }
    return minEval;
  }
}

export function getBestMove(board, size, difficulty, aiPlayer, humanPlayer) {
  const availableMoves = [];
  for (let i = 0; i < board.length; i++) {
    if (board[i] === null) availableMoves.push(i);
  }
  
  if (availableMoves.length === 0) return null;
  
  if (difficulty === 'Easy') {
    return availableMoves[Math.floor(Math.random() * availableMoves.length)];
  }
  
  // Hard mode
  let maxDepth = 4; // default for 4x4 or 5x5
  if (size === 3) {
    maxDepth = 9; // Full minimax for 3x3
  } else if (size === 4) {
    maxDepth = 5;
  } else if (size === 5) {
    maxDepth = 4;
  }
  
  // Dynamically adjust maxDepth based on available moves to prevent UI blocking
  if (size === 5 && availableMoves.length > 20) {
    maxDepth = 3;
  } else if (size === 4 && availableMoves.length > 12) {
    maxDepth = 4;
  }

  let bestScore = -Infinity;
  let bestMove = -1;
  let alpha = -Infinity;
  let beta = Infinity;

  // Small optimization for center preference on first moves if board is empty
  if (availableMoves.length === board.length) {
      if (size === 3) return 4;
      if (size === 5) return 12;
  }

  // Pre-sort moves? Testing central moves first speeds up alpha-beta pruning.
  // For simplicity and speed, we will evaluate directly.
  for (let i = 0; i < board.length; i++) {
    if (board[i] === null) {
      board[i] = aiPlayer;
      const score = minimax(board, maxDepth - 1, false, alpha, beta, size, aiPlayer, humanPlayer);
      board[i] = null;
      
      if (score > bestScore) {
        bestScore = score;
        bestMove = i;
      }
      alpha = Math.max(alpha, bestScore);
    }
  }
  
  return bestMove;
}
