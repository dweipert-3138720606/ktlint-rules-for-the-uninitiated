package de.dweipert.ktlint.rules

import com.pinterest.ktlint.rule.engine.core.api.RuleId
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.RuleExecution
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.createRuleExecutionEditorConfigProperty
import com.pinterest.ktlint.test.KtLintAssertThat
import org.junit.jupiter.api.Test

class BlankLineBeforeReturnRuleTest {
    private val assertThat =
        KtLintAssertThat
            .assertThatRuleBuilder({ BlankLineBeforeReturnRule() })
            .withEditorConfigOverride(
                RuleId("uninitiated:blank-line-before-return").createRuleExecutionEditorConfigProperty(RuleExecution.enabled) to
                    RuleExecution.enabled,
            ).assertThat()

    @Test
    fun `should flag return without preceding blank line`() {
        val code =
            """
            fun f() {
                val x = 1
                return x
            }
            """.trimIndent()
        assertThat(code)
            .hasLintViolationWithoutAutoCorrect(3, 5, "Add a blank line before the return statement")
    }

    @Test
    fun `should not flag return with preceding blank line`() {
        val code =
            """
            fun f() {
                val x = 1

                return x
            }
            """.trimIndent()
        assertThat(code)
            .hasNoLintViolations()
    }

    @Test
    fun `should not flag single statement function`() {
        val code =
            """
            fun f() {
                return value.toString()
            }
            """.trimIndent()
        assertThat(code)
            .hasNoLintViolations()
    }
}
