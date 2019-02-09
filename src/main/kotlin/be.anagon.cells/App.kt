package be.anagon.cells

import kotlin.Any

class App(val automaton: Automaton) {
    val Rules = object {
        val changeListeners = mutableListOf<(Array<Rule>) -> Unit>()
        val selectListeners = mutableListOf<(Rule?) -> Unit>()
        val updateListeners = mutableListOf<(Rule) -> Unit>()
        var selected: Rule? = null

        val types = listOf(
            CustomPatternRule,
            RandomWalkRule
        ).toTypedArray()

        val typesByKey = mapOf<String, () -> Rule>(
            CustomPatternRule.key to { CustomPatternRule.new() },
            RandomWalkRule.key to { RandomWalkRule.new() }
        )

        private val rules = automaton.layers[0].rules

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
        fun newPatternRule(): CustomPatternRule {
            return CustomPatternRule.new()
        }

        @JsName("onChange")
        fun onChange(listener: (Array<Rule>) -> Unit) {
            changeListeners += listener
            listener(rules.toTypedArray())
        }

        @JsName("onUpdate")
        fun onUpdate(listener: (Rule) -> Unit) {
            updateListeners += listener
        }

        @JsName("doUpdate")
        @Deprecated("only allow changing rules through App class", ReplaceWith("App.Rules.___"))
        fun doUpdate(rule: Rule) {
            updateListeners.forEach { it(rule) }
        }

        @JsName("onSelect")
        fun onSelect(listener: (Rule?) -> Unit) {
            selectListeners += listener
            listener(selected)
        }

        private fun doSelect() {
            selectListeners.forEach { it(selected) }
        }

        @JsName("select")
        fun select(rule: Rule?) {
            selected = rule
            doSelect()
        }


        @JsName("clear")
        fun clear() {
            rules.clear()
            doChange()
        }

        @JsName("delete")
        fun delete(rule: Rule) {
            val index = rules.indexOf(rule)
            rules.remove(rule)
            select(rules.getOrNull(index))
            doChange()
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
}