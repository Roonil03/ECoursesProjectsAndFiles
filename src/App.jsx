import React from 'react';
import IntroScreen from './components/IntroScreen';
import GameBoard from './components/GameBoard';
import GameControls from './components/GameControls';
import { useTicTacToe } from './hooks/useTicTacToe';

function App() {
  const {
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
  } = useTicTacToe();

  return (
    <div className="min-h-screen bg-neutral-950 text-neutral-100 py-12 px-4 font-sans selection:bg-teal-500/30">
      <div className="max-w-4xl mx-auto">
        {gameStatus === 'intro' ? (
          <IntroScreen onStart={startGame} />
        ) : (
          <div className="flex flex-col items-center">
            <header className="mb-8 text-center">
              <h1 className="text-3xl font-extrabold text-transparent bg-clip-text bg-gradient-to-r from-teal-400 to-emerald-400 mb-2">
                Tic-Tac-Toe
              </h1>
              <p className="text-neutral-400 text-sm font-medium tracking-wide">
                {gameSettings.size}x{gameSettings.size} • {gameSettings.mode} {gameSettings.mode === 'Single Player' ? `(${gameSettings.difficulty})` : ''}
              </p>
            </header>

            <GameBoard 
              board={board}
              size={gameSettings.size}
              onTileClick={handleTileClick}
              winningLine={winningLine}
              winner={winner}
            />

            <GameControls 
              winner={winner}
              isXNext={isXNext}
              onReset={resetGame}
              onForfeit={forfeitGame}
              onMainMenu={() => setGameStatus('intro')}
              mode={gameSettings.mode}
            />
          </div>
        )}
      </div>
    </div>
  );
}

export default App;
