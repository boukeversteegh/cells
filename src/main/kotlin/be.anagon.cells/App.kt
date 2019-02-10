package be.anagon.cells

import kotlin.js.Json

class App(val automaton: Automaton) {
    val Rules = RulesController()
    val layer: Layer
        get() = automaton.layers[0]

    init {
        // Event propagation
        Rules.updates.observe {
            layer.refresh()
        }
    }

    class Observable<T>(private var initialValue: T? = null, private val triggerOnListen: Boolean) {
        private val listeners = mutableListOf<(T) -> Unit>()

        @JsName("observe")
        @Suppress("unused")
        fun observe(listener: (T) -> Unit) {
            listeners += listener
            if (initialValue != null && triggerOnListen) {
                listener.invoke(initialValue!!)
            }
        }

        fun push(value: T) {
            this.initialValue = value
            listeners.forEach { it.invoke(value) }
        }
    }

    inner class RulesController {
        val changes = Observable(layer.rules.toTypedArray(), true)
        val updates = Observable<IRule>(triggerOnListen = false)

        private val updateListeners = mutableListOf<(IRule) -> Unit>()

        @Suppress("unused")
        @JsName("selected")
        val selected = Observable<IRule?>(null, true)

        val types = listOf(
            EditablePatternRule,
            RandomWalkRule,
            ElectricityRule
        ).toTypedArray()

        val typesByKey = mapOf<String, () -> Rule>(
            EditablePatternRule.key to { EditablePatternRule.new() },
            RandomWalkRule.key to { RandomWalkRule.new() },
            ElectricityRule.key to { ElectricityRule() },
            GameOfLife.key to { GameOfLife() }
        )

        private val rules: MutableList<IRule> = automaton.layers[0].rules

        @Suppress("unused")
        @JsName("getTypes")
        fun getTypes() = types

        @JsName("addRule")
        fun addRule(rule: Rule) {
            automaton.layers[0].addRule(rule)
            doChange()
        }

        @JsName("addByKey")
        fun addByKey(key: String) {
            val factory = typesByKey[key]

            if (factory != null) {
                addRule(factory.invoke())
            }
        }

        @JsName("newPatternRule")
        fun newPatternRule(): EditablePatternRule {
            return EditablePatternRule.new()
        }

        @JsName("update")
        fun update(rule: IRule) {
            updates.push(rule)
        }

        @JsName("select")
        fun select(rule: IRule?) = selected.push(rule)

        @JsName("clear")
        fun clear() {
            rules.clear()
            select(null)
            doChange()
        }

        @JsName("delete")
        fun delete(rule: IRule) {
            val index = rules.indexOf(rule)
            rules.remove(rule)
            select(rules.getOrNull(index))
            doChange()
        }

        @JsName("setName")
        fun setName(rule: EditableNamedRule, name: String) {
            rule.name = name
            update(rule)
        }

        internal fun doChange() = changes.push(rules.toTypedArray())
    }

    @Suppress("unused")
    val CellTypes = CellTypesController()

    inner class CellTypesController {
        val selected = Observable<CellType>(initialValue = Dirt, triggerOnListen = true)
        val changes = Observable(initialValue = layer.getCellTypes(), triggerOnListen = true)

        @JsName("select")
        @Suppress("unused")
        fun select(cellType: CellType) {
            selected.push(cellType)
        }

        @JsName("add")
        fun add() {
            val cellType = CustomCellType("#FF00FF")
            layer.cellTypes.add(cellType)
            changes.push(layer.getCellTypes())
            selected.push(cellType)
            console.log("Added")
        }
    }

    val EditablePatternRules = object {
        @JsName("setInput")
        fun setInput(rule: EditablePatternRule, position: Position, cellType: CellType) {
            rule.setInput(position.x, position.y, cellType)
            Rules.update(rule)
        }

        @JsName("setOutput")
        fun setOutput(rule: EditablePatternRule, position: Position, cellType: CellType) {
            rule.setOutput(position.x, position.y, cellType)
            Rules.update(rule)
        }
    }

    val RandomWalkRules = object {
        @JsName("setCellType")
        fun setCellType(rule: RandomWalkRule, cellType: CellType) {
            rule.cellType = cellType
            Rules.update(rule)
        }
        @JsName("setBackground")
        fun setBackground(rule: RandomWalkRule, cellType: CellType) {
            rule.background = cellType
            Rules.update(rule)
        }
    }


    @Suppress("unused")
    val Layers = object {
        @JsName("load")
        fun load(layerState: Json) {
            JsonMapper().fromJson(layer, layerState)
            Rules.doChange()
        }
    }
}