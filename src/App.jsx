import { useState, useEffect } from 'react';
import './App.css';

const starTypes = [
  { name: 'Red Dwarf', image: '/stars/red_dwarf.png' },
  { name: 'Yellow Dwarf', image: '/stars/yellow_dwarf.png' },
  { name: 'Blue Giant', image: '/stars/blue_giant.png' },
  { name: 'Red Giant', image: '/stars/red_giant.png' },
  { name: 'White Dwarf', image: '/stars/white_dwarf.png' },
  { name: 'Neutron Star', image: '/stars/neutron_star.png' },
  { name: 'Supernova', image: '/stars/supernova.png' },
  { name: 'Pulsar', image: '/stars/pulsar.png' },
];

function App() {
  const [cards, setCards] = useState([]);
  const [flippedIndices, setFlippedIndices] = useState([]);
  const [matchedIndices, setMatchedIndices] = useState([]);
  const [moves, setMoves] = useState(0);
  const [isChecking, setIsChecking] = useState(false);

  // Initialize and shuffle cards
  const initializeGame = () => {
    const duplicatedCards = [...starTypes, ...starTypes];
    const shuffledCards = duplicatedCards
      .sort(() => Math.random() - 0.5)
      .map((card, index) => ({ ...card, id: index }));
    
    setCards(shuffledCards);
    setFlippedIndices([]);
    setMatchedIndices([]);
    setMoves(0);
    setIsChecking(false);
  };

  useEffect(() => {
    initializeGame();
  }, []);

  const handleCardClick = (index) => {
    // Prevent clicking if checking, already flipped, or matched
    if (
      isChecking || 
      flippedIndices.includes(index) || 
      matchedIndices.includes(index)
    ) {
      return;
    }

    const newFlippedIndices = [...flippedIndices, index];
    setFlippedIndices(newFlippedIndices);

    // If two cards are flipped, check for match
    if (newFlippedIndices.length === 2) {
      setIsChecking(true);
      setMoves((prev) => prev + 1);

      const [firstIndex, secondIndex] = newFlippedIndices;
      if (cards[firstIndex].name === cards[secondIndex].name) {
        // Match found
        setMatchedIndices((prev) => [...prev, firstIndex, secondIndex]);
        setFlippedIndices([]);
        setIsChecking(false);
      } else {
        // No match, flip back after a delay
        setTimeout(() => {
          setFlippedIndices([]);
          setIsChecking(false);
        }, 1000);
      }
    }
  };

  const isGameWon = matchedIndices.length === cards.length && cards.length > 0;

  return (
    <div className="app-container">
      <div className="header">
        <h1>Stellar Memory</h1>
        <p>Match the stars of the galaxy</p>
      </div>

      <div className="stats">
        <span>Moves: {moves}</span>
        {isGameWon && <span style={{ color: '#4ade80' }}>You Won! 🎉</span>}
        <button className="reset-btn" onClick={initializeGame}>
          Reset Game
        </button>
      </div>

      <div className="grid">
        {cards.map((card, index) => {
          const isFlipped = flippedIndices.includes(index) || matchedIndices.includes(index);
          const isMatched = matchedIndices.includes(index);
          
          return (
            <div
              key={card.id}
              className={`card ${isFlipped ? 'flipped' : ''} ${isMatched ? 'matched' : ''}`}
              onClick={() => handleCardClick(index)}
              title={isFlipped ? card.name : "Unknown Star"}
            >
              <div className="card-face card-front">
                {/* Back of the card (default state) */}
              </div>
              <div className="card-face card-back">
                {/* Front of the card (revealed) */}
                <img src={card.image} alt={card.name} />
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}

export default App;
