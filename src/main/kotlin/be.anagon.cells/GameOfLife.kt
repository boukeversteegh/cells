package be.anagon.cells

class GameOfLife(private val aliveCellType: CellType = Alive) : Rule(), NamedRule {

    override val name = "Game of Life"

    override val key = Companion.key

    companion object {
        const val key = "GameOfLife"
    }

    override fun evaluate(position: Position, cells: Map<Position, CellType>): Map<Position, CellType> {
        val c = cells[position]

        val aliveCount = cells.minus(position).values.count { it === aliveCellType }

        if (c === aliveCellType) {
            // Any live cell with fewer than two live neighbors dies, as if by underpopulation.
            if (aliveCount < 2) {
                return mapOf(position to None)
            }
            // Any live cell with two or three live neighbors lives on to the next generation.
            if (aliveCount == 2 || aliveCount == 3) {
                return emptyMap()
            }
            // Any live cell with more than three live neighbors dies, as if by overpopulation.
            if (aliveCount > 3) {
                return mapOf(position to None)
            }
        } else {
            // Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.
            if (aliveCount == 3) {
                return mapOf(position to aliveCellType)
            }
        }

        return emptyMap()
    }
}