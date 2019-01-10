package interactive

abstract class PatternRule : Rule() {
    abstract val input: Map<Position, CellType>
    abstract val output: Map<Position, CellType>

    override fun evaluate(position: Position, neighbors: Map<Position, CellType>): Map<Position, CellType> {
        val patternMatches = input.entries.all { neighbors[position + it.key] == it.value }

        return if (patternMatches) (position + output) else emptyMap()
    }
}

private operator fun <V> Position.plus(map: Map<Position, V>): Map<Position, V> {
    return map.map { it ->
        (this + it.key) to it.value
    }.toMap()
}

operator fun Position.plus(position: Position): Position {
    return (x + position.x) to (y + position.y)
}
