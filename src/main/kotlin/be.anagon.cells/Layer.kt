package be.anagon.cells

import kotlin.js.Json
import kotlin.js.json

class Layer(private val w: Int, private val h: Int) {
    val cellTypes = mutableListOf<CellType>()
    val rules = mutableListOf<Rule>()
    private val cells = Array(h) { Array<CellType>(w) { None } }

    private val changes = mutableSetOf<Position>()

    private var evaluateEntireLayer = false

    @JsName("ruleUpdated")
    fun ruleUpdated() {
        evaluateEntireLayer = true
    }

    @JsName("get")
    fun get(x: Int, y: Int): CellType {
        if (y !in 0 until h || x !in 0 until w) {
            return Void
        }
        return cells[y][x]
    }

    @JsName("set")
    fun set(x: Int, y: Int, cellType: CellType) {
        if (y in 0 until h && x in 0 until w && cellType !is Any) {
            cells[y][x] = cellType
            changes.add(Position(x, y))
        }
    }

    private fun get(position: Position): CellType {
        return this.get(position.x, position.y)
    }

    private fun get(positions: List<Position>): Map<Position, CellType> {
        return positions.map { it to get(it) }.toMap()
    }

    @JsName("setByIndex")
    fun set(x: Int, y: Int, cellTypeIndex: Int) {
        set(x, y, cellTypes[cellTypeIndex])
    }

    @JsName("clear")
    fun clear() {
        for (y in 0 until h) {
            for (x in 0 until w) {
                cells[y][x] = None
            }
        }
    }

    @JsName("getCellTypes")
    fun getCellTypes(): Array<CellType> {
        return cellTypes.toTypedArray()
    }

    @JsName("getRules")
    fun getRules(): Array<Rule> {
        return rules.toTypedArray()
    }

    @JsName("addRule")
    fun addRule(): EditablePatternRule {
        val rule = EditablePatternRule(emptyMap(), emptyMap())
        addRule(rule)
        return rule
    }

    fun addRule(rule: Rule) {
        rules.add(rule)
        evaluateEntireLayer = true
    }

    @JsName("deleteRule")
    fun deleteRule(rule: Rule) {
        rules.remove(rule)
        evaluateEntireLayer = true
    }

    @JsName("addCellType")
    fun addCellType(color: String = "#FF00FF") {
        cellTypes.add(CustomCellType(color))
    }

    private fun getLastChanges(): Set<Position> {
        return if (evaluateEntireLayer) {
            evaluateEntireLayer = false
            val allPositions = (0 until h).toList().flatMap { y ->
                (0 until w).map { x -> pos(x, y) }
            }.toSet()
            allPositions
        } else {
            changes.toSet()
        }
    }

    fun iterate() {
        // todo: re-evaluate entire grid when changing rules
        val lastChanges = getLastChanges()
        changes.clear()

        val positionsToCheck = lastChanges.flatMap { p ->
            listOf(
                p,
                p.left,
                p.right,
                p.below,
                p.above,
                pos(p.x - 1, p.y - 1),
                pos(p.x - 1, p.y + 1),
                pos(p.x + 1, p.y - 1),
                pos(p.x + 1, p.y + 1)
            )
        }.toSet()

        val changes = mutableMapOf<Position, CellType>()
        for ((x, y) in positionsToCheck) {
            val p = pos(x, y)

            val neighborPositions: List<Position> = listOf(
                p,
                p.left,
                p.right,
                p.below,
                p.above,
                pos(x - 1, y - 1),
                pos(x - 1, y + 1),
                pos(x + 1, y - 1),
                pos(x + 1, y + 1)
            )

            val neighbors: Map<Position, CellType> = get(neighborPositions)

            for (rule in rules) {
                changes.putAll(rule.evaluate(p, neighbors))
            }
        }

        for ((pos, cellType) in changes) {
            val (x, y) = pos
            set(x, y, cellType)
        }

        this.changes.addAll(changes.keys)
    }

    @JsName("serialize")
    fun serialize(): Json {
        return json(
            "cellTypes" to cellTypes.map { it.serialize() }.toTypedArray(),
            "rules" to rules
                .filter { it is SerializableRule }
                .map { it as SerializableRule }
                .map { it.serialize(cellTypes) }
                .toTypedArray(),
            "cells" to cells.map { row ->
                row.map {
                    it.serialize(cellTypes)
                }.toTypedArray()
            }.toTypedArray()
        )
    }
}

val Position.left: Position
    get() = Position(x - 1, y)

val Position.above: Position
    get() = Position(x, y - 1)

val Position.below: Position
    get() = Position(x, y + 1)

val Position.right: Position
    get() = Position(x + 1, y)
