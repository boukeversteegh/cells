package interactive

import kotlin.js.Json

interface SerializableRule {
    val key: String

    fun serialize(cellTypes: List<CellType>): Json
}