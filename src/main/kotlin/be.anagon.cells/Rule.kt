package be.anagon.cells


interface IRule {
    val id: Int
    val key: String

    fun evaluate(position: Position, neighbors: Map<Position, CellType>): Map<Position, CellType>

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
