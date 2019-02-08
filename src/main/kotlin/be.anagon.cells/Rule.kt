package be.anagon.cells

abstract class Rule {
    val id = nextId++
    abstract fun evaluate(position: Position, neighbors: Map<Position, CellType>): Map<Position, CellType>

    companion object {
        var nextId = 1
    }
}
