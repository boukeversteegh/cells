package be.anagon.cells

import kotlin.js.Json

class App(val automaton: Automaton) {
    val Rules = RulesController()
    val layer: Layer
        get() = automaton.layers[0]

    class Observable<T>(private var initialValue: T, private val triggerOnListen: Boolean) {
        private val listeners = mutableListOf<(T) -> Unit>()

        @JsName("observe")
        @Suppress("unused")
        fun observe(listener: (T) -> Unit) {
            listeners += listener
            if (triggerOnListen) {
                listener.invoke(initialValue)
            }
        }

        fun push(value: T) {
            this.initialValue = value
            listeners.forEach { it.invoke(value) }
        }
    }

    inner class RulesController {
        val changes = Observable(layer.rules.toTypedArray(), true)

        private val changeListeners = mutableListOf<(Array<IRule>) -> Unit>()
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

        @JsName("onUpdate")
        fun onUpdate(listener: (IRule) -> Unit) {
            updateListeners += listener
        }

        @JsName("doUpdate")
        fun doUpdate(rule: IRule) {
            updateListeners.forEach { it(rule) }
            layer.refresh()
        }

        @JsName("select")
        fun select(rule: IRule?) = selected.push(rule)

        @JsName("clear")
        fun clear() {
            rules.clear()
            selected.push(null)
            doChange()
        }

        @JsName("delete")
        fun delete(rule: IRule) {
            val index = rules.indexOf(rule)
            rules.remove(rule)
            selected.push(rules.getOrNull(index))
            doChange()
        }

        @JsName("setName")
        fun setName(rule: EditableNamedRule, name: String) {
            rule.name = name
            doUpdate(rule)
        }

        internal fun doChange() = changes.push(rules.toTypedArray())
    }

    @Suppress("unused")
    val CellTypes = CellTypesController()

    inner class CellTypesController {
        val selected = Observable<CellType>(initialValue = Dirt, triggerOnListen = true)

        @JsName("select")
        @Suppress("unused")
        fun select(cellType: CellType) {
            selected.push(cellType)
        }
    }

    val EditablePatternRules = object {
        @JsName("setInput")
        fun setInput(rule: EditablePatternRule, position: Position, cellType: CellType) {
            rule.setInput(position.x, position.y, cellType)
            Rules.doUpdate(rule)
        }

        @JsName("setOutput")
        fun setOutput(rule: EditablePatternRule, position: Position, cellType: CellType) {
            rule.setOutput(position.x, position.y, cellType)
            Rules.doUpdate(rule)
        }
    }

    val RandomWalkRules = object {
        @JsName("setCellType")
        fun setCellType(rule: RandomWalkRule, cellType: CellType) {
            rule.cellType = cellType
            Rules.doUpdate(rule)
        }
        @JsName("setBackground")
        fun setBackground(rule: RandomWalkRule, cellType: CellType) {
            rule.background = cellType
            Rules.doUpdate(rule)
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