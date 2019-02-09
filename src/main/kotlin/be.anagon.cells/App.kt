package be.anagon.cells

class App(val automaton: Automaton) {
    val Rules = RulesController()
    val layer = automaton.layers[0]

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

        fun set(value: T) {
            this.initialValue = value
            listeners.forEach { it.invoke(value) }
        }
    }

    inner class RulesController {
        private val changeListeners = mutableListOf<(Array<IRule>) -> Unit>()
        private val selectListeners = mutableListOf<(IRule?) -> Unit>()
        private val updateListeners = mutableListOf<(IRule) -> Unit>()

        var selected: IRule? = null

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

        @JsName("onChange")
        fun onChange(listener: (Array<IRule>) -> Unit) {
            changeListeners += listener
            listener(rules.toTypedArray())
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

        @JsName("onSelect")
        fun onSelect(listener: (IRule?) -> Unit) {
            selectListeners += listener
            listener(selected)
        }

        private fun doSelect() {
            selectListeners.forEach { it(selected) }
        }

        @JsName("select")
        fun select(rule: IRule?) {
            selected = rule
            doSelect()
        }


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
            doUpdate(rule)
        }

        private fun doChange() {
            val rules = rules.toTypedArray()
            changeListeners.forEach { it(rules) }
        }
    }

    @Suppress("unused")
    val CellTypes = object {
        val selected = Observable<CellType>(initialValue = Dirt, triggerOnListen = true)

        @JsName("select")
        @Suppress("unused")
        fun select(cellType: CellType) {
            selected.set(cellType)
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
}