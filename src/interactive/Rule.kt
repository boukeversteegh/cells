package interactive

abstract class Rule {
//    abstract val pattern: Map<Position, CellType>

    abstract fun evaluate(position: Position, neighbors: Map<Position, CellType>): Map<Position, CellType>
}
