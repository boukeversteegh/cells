package interactive

abstract class Rule {
    abstract fun evaluate(position: Position, neighbors: Map<Position, CellType>): Map<Position, CellType>
}
