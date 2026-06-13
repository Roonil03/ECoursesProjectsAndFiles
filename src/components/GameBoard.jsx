import React from 'react';
import Tile from './Tile';
import { motion } from 'framer-motion';

export default function GameBoard({ board, size, onTileClick, winningLine, winner }) {
  return (
    <motion.div 
      initial={{ opacity: 0, scale: 0.95 }}
      animate={{ opacity: 1, scale: 1 }}
      className="w-full max-w-2xl mx-auto bg-neutral-900 p-4 md:p-8 rounded-3xl shadow-2xl border border-neutral-800"
    >
      <div 
        className="grid gap-2 md:gap-4 w-full"
        style={{
          gridTemplateColumns: `repeat(${size}, minmax(0, 1fr))`
        }}
      >
        {board.map((cell, index) => (
          <Tile 
            key={index}
            value={cell}
            onClick={() => onTileClick(index)}
            isWinningTile={winningLine.includes(index)}
            disabled={cell !== null || winner !== null}
          />
        ))}
      </div>
    </motion.div>
  );
}
