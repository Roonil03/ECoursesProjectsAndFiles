import React, { useState } from 'react';
import { motion } from 'framer-motion';

export default function IntroScreen({ onStart }) {
  const [size, setSize] = useState(3);
  const [mode, setMode] = useState('Two Players');
  const [difficulty, setDifficulty] = useState('Easy');

  return (
    <motion.div 
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="flex flex-col items-center justify-center min-h-[80vh] w-full max-w-md mx-auto p-6 bg-neutral-800 rounded-2xl shadow-2xl border border-neutral-700"
    >
      <h1 className="text-4xl font-extrabold text-transparent bg-clip-text bg-gradient-to-r from-teal-400 to-emerald-400 mb-8">
        Tic-Tac-Toe
      </h1>
      
      <div className="w-full mb-6">
        <label className="block text-neutral-300 mb-2 font-semibold text-sm tracking-wide uppercase">Board Size</label>
        <div className="flex gap-2">
          {[3, 4, 5].map((s) => (
            <button
              key={s}
              onClick={() => setSize(s)}
              className={`flex-1 py-3 rounded-lg font-bold transition-all duration-200 ${size === s ? 'bg-teal-600 text-white shadow-lg shadow-teal-500/30' : 'bg-neutral-700 text-neutral-400 hover:bg-neutral-600'}`}
            >
              {s}x{s}
            </button>
          ))}
        </div>
      </div>

      <div className="w-full mb-6">
        <label className="block text-neutral-300 mb-2 font-semibold text-sm tracking-wide uppercase">Game Mode</label>
        <div className="flex gap-2">
          {['Two Players', 'Single Player'].map((m) => (
            <button
              key={m}
              onClick={() => setMode(m)}
              className={`flex-1 py-3 rounded-lg font-bold transition-all duration-200 ${mode === m ? 'bg-teal-600 text-white shadow-lg shadow-teal-500/30' : 'bg-neutral-700 text-neutral-400 hover:bg-neutral-600'}`}
            >
              {m}
            </button>
          ))}
        </div>
      </div>

      {mode === 'Single Player' && (
        <motion.div 
          initial={{ opacity: 0, height: 0 }}
          animate={{ opacity: 1, height: 'auto' }}
          className="w-full mb-8"
        >
          <label className="block text-neutral-300 mb-2 font-semibold text-sm tracking-wide uppercase">Bot Difficulty</label>
          <div className="flex gap-2">
            {['Easy', 'Hard'].map((d) => (
              <button
                key={d}
                onClick={() => setDifficulty(d)}
                className={`flex-1 py-3 rounded-lg font-bold transition-all duration-200 ${difficulty === d ? 'bg-teal-600 text-white shadow-lg shadow-teal-500/30' : 'bg-neutral-700 text-neutral-400 hover:bg-neutral-600'}`}
              >
                {d}
              </button>
            ))}
          </div>
        </motion.div>
      )}

      <button
        onClick={() => onStart(size, mode, difficulty)}
        className="w-full py-4 mt-4 bg-gradient-to-r from-teal-500 to-emerald-500 hover:from-teal-400 hover:to-emerald-400 text-white rounded-xl font-bold text-lg shadow-xl shadow-teal-500/20 transform transition-all hover:scale-[1.02] active:scale-[0.98]"
      >
        Start Game
      </button>
    </motion.div>
  );
}
