package be.anagon.cells

abstract class Rule {
    val id = nextId++
    abstract fun evaluate(position: Position, neighbors: Map<Position, CellType>): Map<Position, CellType>

    @JsName("isEditable")
    open fun isEditable() = false

    companion object {
        var nextId = 1
    }
}
