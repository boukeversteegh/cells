package be.anagon.cells


interface IRule {
    val id: Int
    val key: String

    fun evaluate(position: Position, cells: Map<Position, CellType>): Map<Position, CellType>

    @JsName("isEditable")
    fun isEditable(): Boolean

    open fun evaluate(position: Position, cells: Array<Array<CellType>>): Map<Position, CellType> {
        val map = mutableMapOf<Position, CellType>()
        cells.forEachIndexed { y, row ->
            row.forEachIndexed { x, cellType ->
                map[pos(x, y)] = cellType
            }
        }
        return evaluate(position, map)
    }
}

abstract class Rule : IRule {
    override val id = nextId++

    override fun isEditable() = false

    companion object {
        var nextId = 1
    }
}
