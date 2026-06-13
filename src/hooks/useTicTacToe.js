import { useState, useEffect, useCallback } from 'react';
import { calculateWinner } from '../utils/gameLogic';
import { getBestMove } from '../utils/aiEngine';

export function useTicTacToe() {
  const [gameStatus, setGameStatus] = useState('intro'); // 'intro' or 'playing'
  const [gameSettings, setGameSettings] = useState({ size: 3, mode: 'Two Players', difficulty: 'Easy' });
  const [board, setBoard] = useState([]);
  const [isXNext, setIsXNext] = useState(true);
  const [winner, setWinner] = useState(null);
  const [winningLine, setWinningLine] = useState([]);

  const startGame = useCallback((size, mode, difficulty) => {
    setGameSettings({ size, mode, difficulty });
    setBoard(Array(size * size).fill(null));
    setIsXNext(true);
    setWinner(null);
    setWinningLine([]);
    setGameStatus('playing');
  }, []);

  const resetGame = useCallback(() => {
    setBoard(Array(gameSettings.size * gameSettings.size).fill(null));
    setIsXNext(true);
    setWinner(null);
    setWinningLine([]);
  }, [gameSettings.size]);

  const forfeitGame = useCallback(() => {
    // If someone forfeits, the opponent wins
    setWinner(isXNext ? 'O' : 'X');
  }, [isXNext]);

  const handleTileClick = useCallback((index) => {
    if (board[index] || winner) return;

    const newBoard = [...board];
    newBoard[index] = isXNext ? 'X' : 'O';
    setBoard(newBoard);
    setIsXNext(!isXNext);
  }, [board, isXNext, winner]);

  // AI Turn Handling
  useEffect(() => {
    if (gameStatus === 'playing' && gameSettings.mode === 'Single Player' && !winner && !isXNext) {
      // It's O's turn (AI). Use a small timeout so the UI paints the player's move first.
      const timer = setTimeout(() => {
        const bestMoveIndex = getBestMove(board, gameSettings.size, gameSettings.difficulty, 'O', 'X');
        if (bestMoveIndex !== null && bestMoveIndex !== -1) {
          const newBoard = [...board];
          newBoard[bestMoveIndex] = 'O';
          setBoard(newBoard);
          setIsXNext(true);
        }
      }, 50);
      return () => clearTimeout(timer);
    }
  }, [board, gameStatus, gameSettings, isXNext, winner]);

  // Winner Checking
  useEffect(() => {
    if (gameStatus === 'playing' && !winner) {
      const result = calculateWinner(board, gameSettings.size);
      if (result) {
        setWinner(result.winner);
        setWinningLine(result.line);
      }
    }
  }, [board, gameStatus, gameSettings.size, winner]);

  return {
    gameStatus,
    setGameStatus,
    gameSettings,
    board,
    isXNext,
    winner,
    winningLine,
    startGame,
    resetGame,
    forfeitGame,
    handleTileClick
  };
}
