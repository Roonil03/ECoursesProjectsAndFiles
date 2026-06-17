package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard = BaseSquareBoard(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = BaseGameBoard(width)

// Add your implementations here
open class BaseSquareBoard(override val width: Int) : SquareBoard {
    protected val cells: List<Cell> = (1..width).flatMap { i ->
        (1..width).map { j -> Cell(i, j) }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        if (i !in 1..width || j !in 1..width) return null
        return cells[(i - 1) * width + (j - 1)]
    }

    override fun getCell(i: Int, j: Int): Cell {
        return getCellOrNull(i, j)
            ?: throw IllegalArgumentException("Cell coordinates ($i, $j) are out of bounds for width $width")
    }

    override fun getAllCells(): Collection<Cell> = cells

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        return jRange.mapNotNull { j -> getCellOrNull(i, j) }
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        return iRange.mapNotNull { i -> getCellOrNull(i, j) }
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when (direction) {
            UP -> getCellOrNull(i - 1, j)
            DOWN -> getCellOrNull(i + 1, j)
            LEFT -> getCellOrNull(i, j - 1)
            RIGHT -> getCellOrNull(i, j + 1)
        }
    }
}

class BaseGameBoard<T>(width: Int) : BaseSquareBoard(width), GameBoard<T> {
    private val cellValues = mutableMapOf<Cell, T?>()

    override fun get(cell: Cell): T? = cellValues[cell]

    override fun set(cell: Cell, value: T?) {
        cellValues[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return cells.filter { predicate(get(it)) }
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return cells.find { predicate(get(it)) }
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return cells.any { predicate(get(it)) }
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return cells.all { predicate(get(it)) }
    }
}