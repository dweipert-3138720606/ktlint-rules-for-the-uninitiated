package de.dweipert.ktlint.rules

import com.pinterest.ktlint.rule.engine.core.api.RuleId
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.RuleExecution
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.createRuleExecutionEditorConfigProperty
import com.pinterest.ktlint.test.KtLintAssertThat
import org.junit.jupiter.api.Test

class NoToRuleTest {
    private val assertThat =
        KtLintAssertThat
            .assertThatRuleBuilder({ NoToRule() })
            .withEditorConfigOverride(
                RuleId("uninitiated:no-to")
                    .createRuleExecutionEditorConfigProperty(RuleExecution.enabled) to
                    RuleExecution.enabled,
            ).assertThat()

    @Test
    fun `should flag infix to`() {
        assertThat("fun f() { val x = \"a\" to 1 }")
            .hasLintViolationWithoutAutoCorrect(
                1,
                23,
                "Use 'Pair' instead of 'to'",
            )
    }

    @Test
    fun `should flag to inside mapOf`() {
        assertThat("fun f() { val x = mapOf(\"a\" to 1) }")
            .hasLintViolationWithoutAutoCorrect(
                1,
                29,
                "Use 'Pair' instead of 'to'",
            )
    }

    @Test
    fun `should flag standalone key to value`() {
        assertThat("fun f() { val x = \"key\" to \"value\" }")
            .hasLintViolationWithoutAutoCorrect(
                1,
                25,
                "Use 'Pair' instead of 'to'",
            )
    }

    @Test
    fun `should flag parenthesized to`() {
        assertThat("fun f() { val x = (a to b) }")
            .hasLintViolationWithoutAutoCorrect(
                1,
                22,
                "Use 'Pair' instead of 'to'",
            )
    }

    @Test
    fun `should not flag explicit Pair`() {
        assertThat("fun f() { val x = Pair(\"a\", 1) }")
            .hasNoLintViolations()
    }

    @Test
    fun `should not flag unrelated expression`() {
        assertThat("fun f() { val x = 1 }")
            .hasNoLintViolations()
    }
}
