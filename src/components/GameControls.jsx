import React from 'react';
import { motion } from 'framer-motion';
import { RefreshCcw, Home, Flag } from 'lucide-react';

export default function GameControls({ winner, isXNext, onReset, onForfeit, onMainMenu, mode }) {
  return (
    <motion.div 
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="w-full max-w-2xl mx-auto mt-8 p-6 bg-neutral-800 rounded-2xl shadow-xl border border-neutral-700 flex flex-col sm:flex-row items-center justify-between gap-4"
    >
      <div className="flex-1 text-center sm:text-left">
        {winner ? (
          <h2 className="text-2xl font-bold">
            {winner === 'Draw' ? (
              <span className="text-neutral-300">It's a Draw!</span>
            ) : (
              <span className="text-emerald-400">Player {winner} Wins!</span>
            )}
          </h2>
        ) : (
          <h2 className="text-xl font-semibold text-neutral-300 flex items-center justify-center sm:justify-start gap-2">
            Current Turn: 
            <span className={`font-bold ${isXNext ? 'text-teal-400' : 'text-rose-400'}`}>
              {isXNext ? 'X' : 'O'}
              {mode === 'Single Player' && !isXNext && ' (Bot)'}
            </span>
          </h2>
        )}
      </div>

      <div className="flex items-center gap-3 w-full sm:w-auto">
        {!winner && (
          <button 
            onClick={onForfeit}
            className="flex-1 sm:flex-none flex items-center justify-center gap-2 px-4 py-2 bg-rose-500/10 text-rose-400 hover:bg-rose-500/20 rounded-lg font-medium transition-colors"
          >
            <Flag size={18} /> Forfeit
          </button>
        )}
        <button 
          onClick={onReset}
          className="flex-1 sm:flex-none flex items-center justify-center gap-2 px-4 py-2 bg-teal-500/10 text-teal-400 hover:bg-teal-500/20 rounded-lg font-medium transition-colors"
        >
          <RefreshCcw size={18} /> {winner ? 'Play Again' : 'Restart'}
        </button>
        <button 
          onClick={onMainMenu}
          className="flex-1 sm:flex-none flex items-center justify-center gap-2 px-4 py-2 bg-neutral-700 text-neutral-300 hover:bg-neutral-600 rounded-lg font-medium transition-colors"
        >
          <Home size={18} /> Menu
        </button>
      </div>
    </motion.div>
  );
}
