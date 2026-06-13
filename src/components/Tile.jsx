import React from 'react';
import { motion } from 'framer-motion';
import { X, Circle } from 'lucide-react';

export default function Tile({ value, onClick, isWinningTile, disabled }) {
  return (
    <button
      onClick={onClick}
      disabled={disabled}
      className={`
        relative flex items-center justify-center w-full aspect-square rounded-xl 
        transition-all duration-200 shadow-inner
        ${isWinningTile ? 'bg-emerald-500 shadow-emerald-500/50 scale-105 z-10' : 'bg-neutral-800 hover:bg-neutral-700'}
        ${disabled && !value ? 'cursor-default' : 'cursor-pointer'}
        border border-neutral-700/50 overflow-hidden
      `}
    >
      {value && (
        <motion.div
          initial={{ opacity: 0, scale: 0.3, rotate: -45 }}
          animate={{ opacity: 1, scale: 1, rotate: 0 }}
          transition={{ type: "spring", stiffness: 300, damping: 20 }}
        >
          {value === 'X' ? (
            <X className={`w-12 h-12 md:w-16 md:h-16 ${isWinningTile ? 'text-white' : 'text-teal-400'}`} strokeWidth={2.5} />
          ) : (
            <Circle className={`w-10 h-10 md:w-14 md:h-14 ${isWinningTile ? 'text-white' : 'text-rose-400'}`} strokeWidth={3} />
          )}
        </motion.div>
      )}
    </button>
  );
}
