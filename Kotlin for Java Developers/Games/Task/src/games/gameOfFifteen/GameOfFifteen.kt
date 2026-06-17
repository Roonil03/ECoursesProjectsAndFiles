package games.gameOfFifteen

import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */

fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
    GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        val permutation = initializer.initialPermutation
        var index = 0
        for (i in 1..4) {
            for (j in 1..4) {
                if (index < permutation.size) {
                    board[board.getCell(i, j)] = permutation[index++]
                } else {
                    board[board.getCell(i, j)] = null
                }
            }
        }
    }

    override fun canMove(): Boolean = true

    override fun hasWon(): Boolean {
        var expectedValue = 1
        for (i in 1..4) {
            for (j in 1..4) {
                if (i == 4 && j == 4) {
                    return board[board.getCell(i, j)] == null
                }
                if (board[board.getCell(i, j)] != expectedValue++) {
                    return false
                }
            }
        }
        return true
    }

    override fun processMove(direction: Direction) {
        val emptyCell = board.find { it == null } ?: return

        val neighborDirection = when (direction) {
            Direction.UP -> Direction.DOWN
            Direction.DOWN -> Direction.UP
            Direction.LEFT -> Direction.RIGHT
            Direction.RIGHT -> Direction.LEFT
        }

        val neighborCell = with(board) { emptyCell.getNeighbour(neighborDirection) }
        if (neighborCell != null) {
            board[emptyCell] = board[neighborCell]
            board[neighborCell] = null
        }
    }

    override fun get(i: Int, j: Int): Int? {
        val cell = board.getCellOrNull(i, j)
        return if (cell != null) board[cell] else null
    }
}