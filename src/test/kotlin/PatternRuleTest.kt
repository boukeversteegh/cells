import be.anagon.cells.CustomPatternRule
import be.anagon.cells.Dirt
import be.anagon.cells.None
import be.anagon.cells.Position
import kotlin.test.Test
import kotlin.test.assertEquals

class PatternRuleTest {

    @Test
    fun evaluate() {
        val rule = CustomPatternRule(
            mapOf(
                Position(0, 0) to None
            ), mapOf(
                Position(0, 0) to Dirt
            )
        )

        val output = rule.evaluate(Position(10, 10), mapOf(Position(10, 10) to None))

        assertEquals(mapOf(Position(10, 10) to Dirt), output)
    }
}