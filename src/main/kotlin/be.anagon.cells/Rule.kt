package be.anagon.cells

import be.anagon.cells.CellType
import be.anagon.cells.Position

abstract class Rule {
    abstract fun evaluate(position: Position, neighbors: Map<Position, CellType>): Map<Position, CellType>
}
