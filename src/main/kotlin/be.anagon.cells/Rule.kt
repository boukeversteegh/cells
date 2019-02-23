package be.anagon.cells


interface IRule {
    val id: Int
    val key: String

    fun evaluate(position: Position, cells: Map<Position, CellType>): Map<Position, CellType>

    fun evaluate(position: Position, cells: Map<Position, CellType>, changes: MutableMap<Position, CellType>) {
        changes.putAll(evaluate(position, cells))
    }

    fun evaluate(position: Position, cells: Array<Array<CellType>>, changes: MutableMap<Position, CellType>) {
        val map = mutableMapOf<Position, CellType>()
        cells.forEachIndexed { y, row ->
            row.forEachIndexed { x, cellType ->
                map[pos(x, y)] = cellType
            }
        }
        return evaluate(position, map, changes)
    }

    @JsName("isEditable")
    fun isEditable(): Boolean
}

abstract class Rule : IRule {
    override val id = nextId++

    override fun isEditable() = false

    companion object {
        var nextId = 1
    }
}
