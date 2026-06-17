package games.game2048

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Your task is to implement the game 2048 https://en.wikipedia.org/wiki/2048_(video_game).
 * Implement the utility methods below.
 *
 * After implementing it you can try to play the game running 'PlayGame2048'.
 */
fun newGame2048(initializer: Game2048Initializer<Int> = RandomGame2048Initializer): Game =
        Game2048(initializer)

class Game2048(private val initializer: Game2048Initializer<Int>) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        repeat(2) {
            board.addNewValue(initializer)
        }
    }

    override fun canMove(): Boolean {
        if (board.any { it == null }) return true
        for (cell in board.getAllCells()) {
            val value = board[cell]
            for (dir in Direction.values()) {
                val neighbor = with(board) { cell.getNeighbour(dir) }
                if (neighbor != null && board[neighbor] == value) {
                    return true
                }
            }
        }
        return false
    }

    override fun hasWon() = board.any { it == 2048 }

    override fun processMove(direction: Direction) {
        if (board.moveValues(direction)) {
            board.addNewValue(initializer)
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }
}

/*
 * Add a new value produced by 'initializer' to a specified cell in a board.
 */
fun GameBoard<Int?>.addNewValue(initializer: Game2048Initializer<Int>) {
    val next = initializer.nextValue(this)
    if (next != null) {
        this[next.first] = next.second
    }
}

/*
 * Update the values stored in a board,
 * so that the values were "moved" in a specified rowOrColumn only.
 * Use the helper function 'moveAndMergeEqual' (in Game2048Helper.kt).
 * The values should be moved to the beginning of the row (or column),
 * in the same manner as in the function 'moveAndMergeEqual'.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValuesInRowOrColumn(rowOrColumn: List<Cell>): Boolean {
    val values = rowOrColumn.map { this[it] }.filterNotNull()
    val mergedValues = values.moveAndMergeEqual { it * 2 }

    var changed = false
    for (i in rowOrColumn.indices) {
        val cell = rowOrColumn[i]
        val newValue = mergedValues.getOrNull(i)
        if (this[cell] != newValue) {
            changed = true
        }
        this[cell] = newValue
    }
    return changed
}

/*
 * Update the values stored in a board,
 * so that the values were "moved" to the specified direction
 * following the rules of the 2048 game .
 * Use the 'moveValuesInRowOrColumn' function above.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValues(direction: Direction): Boolean {
    var changed = false
    when (direction) {
        Direction.UP -> {
            for (j in 1..width) {
                if (moveValuesInRowOrColumn(getColumn(1..width, j))) changed = true
            }
        }
        Direction.DOWN -> {
            for (j in 1..width) {
                if (moveValuesInRowOrColumn(getColumn(width downTo 1, j))) changed = true
            }
        }
        Direction.LEFT -> {
            for (i in 1..width) {
                if (moveValuesInRowOrColumn(getRow(i, 1..width))) changed = true
            }
        }
        Direction.RIGHT -> {
            for (i in 1..width) {
                if (moveValuesInRowOrColumn(getRow(i, width downTo 1))) changed = true
            }
        }
    }
    return changed
}