package be.anagon.cells

import kotlin.Any

class App(val automaton: Automaton) {
    val Rules = RulesController()
    val layer = automaton.layers[0]

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
            ElectricityRule.key to { ElectricityRule() }
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

    val CellTypes = object {
        var selected: CellType = Dirt

        val onSelectListeners = mutableListOf<(CellType) -> Unit>()

        @JsName("select")
        fun select(cellType: CellType) {
            selected = cellType
            doSelect()
        }

        @JsName("onSelect")
        fun onSelect(listener: (CellType) -> Unit) {
            onSelectListeners += listener
            listener(selected)
        }

        private fun doSelect() {
            onSelectListeners.forEach { it(selected) }
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