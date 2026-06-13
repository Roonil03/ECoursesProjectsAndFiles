export function calculateWinner(board, size) {
  // Check rows
  for (let i = 0; i < size; i++) {
    const rowStart = i * size;
    const first = board[rowStart];
    if (first) {
      let isWin = true;
      for (let j = 1; j < size; j++) {
        if (board[rowStart + j] !== first) {
          isWin = false;
          break;
        }
      }
      if (isWin) {
        return { winner: first, line: Array.from({ length: size }, (_, j) => rowStart + j) };
      }
    }
  }

  // Check columns
  for (let i = 0; i < size; i++) {
    const first = board[i];
    if (first) {
      let isWin = true;
      for (let j = 1; j < size; j++) {
        if (board[i + j * size] !== first) {
          isWin = false;
          break;
        }
      }
      if (isWin) {
        return { winner: first, line: Array.from({ length: size }, (_, j) => i + j * size) };
      }
    }
  }

  // Check diagonal (top-left to bottom-right)
  const firstMainDiag = board[0];
  if (firstMainDiag) {
    let isWin = true;
    for (let j = 1; j < size; j++) {
      if (board[j * size + j] !== firstMainDiag) {
        isWin = false;
        break;
      }
    }
    if (isWin) {
      return { winner: firstMainDiag, line: Array.from({ length: size }, (_, j) => j * size + j) };
    }
  }

  // Check diagonal (top-right to bottom-left)
  const firstAntiDiag = board[size - 1];
  if (firstAntiDiag) {
    let isWin = true;
    for (let j = 1; j < size; j++) {
      if (board[j * size + (size - 1 - j)] !== firstAntiDiag) {
        isWin = false;
        break;
      }
    }
    if (isWin) {
      return { winner: firstAntiDiag, line: Array.from({ length: size }, (_, j) => j * size + (size - 1 - j)) };
    }
  }

  // Check draw
  if (!board.includes(null)) {
    return { winner: 'Draw', line: [] };
  }

  return null;
}
