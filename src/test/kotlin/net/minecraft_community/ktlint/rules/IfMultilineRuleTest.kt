package net.minecraft_community.ktlint.rules

import com.pinterest.ktlint.rule.engine.core.api.RuleId
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.RuleExecution
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.createRuleExecutionEditorConfigProperty
import com.pinterest.ktlint.test.KtLintAssertThat
import com.pinterest.ktlint.test.LintViolation
import org.junit.jupiter.api.Test

class IfMultilineRuleTest {
    private val assertThat = KtLintAssertThat.assertThatRuleBuilder { IfMultilineRule() }
        .withEditorConfigOverride(
            RuleId("uninitiated:if-multiline").createRuleExecutionEditorConfigProperty(RuleExecution.enabled) to RuleExecution.enabled,
        )
        .assertThat()

    @Test
    fun `should flag single-line if without braces`() {
        assertThat("fun f() { if (bool) doThing() }")
            .hasLintViolationWithoutAutoCorrect(1, 11, "If statement must be multiline")
    }

    @Test
    fun `should flag single-line if with braces`() {
        assertThat("fun f() { if (bool) { doThing() } }")
            .hasLintViolationWithoutAutoCorrect(1, 11, "If statement must be multiline")
    }

    @Test
    fun `should not flag multiline if with braces`() {
        val code = """
            fun f() {
                if (bool) {
                    doThing()
                }
            }
        """.trimIndent()
        assertThat(code)
            .hasNoLintViolations()
    }

    @Test
    fun `should flag single-line if-else with braces`() {
        assertThat("fun f() { if (x) { a() } else { b() } }")
            .hasLintViolationWithoutAutoCorrect(1, 11, "If statement must be multiline")
    }

    @Test
    fun `should not flag multiline if-else with braces`() {
        val code = """
            fun f() {
                if (x) {
                    a()
                } else {
                    b()
                }
            }
        """.trimIndent()
        assertThat(code)
            .hasNoLintViolations()
    }

    @Test
    fun `should flag single-line else-if with braces`() {
        assertThat("fun f() { if (x) { a() } else if (y) { b() } }")
            .hasLintViolationsWithoutAutoCorrect(
                LintViolation(1, 11, "If statement must be multiline", false),
                LintViolation(1, 31, "If statement must be multiline", false),
            )
    }

    @Test
    fun `should not flag multiline else-if with braces`() {
        val code = """
            fun f() {
                if (x) {
                    a()
                } else if (y) {
                    b()
                }
            }
        """.trimIndent()
        assertThat(code)
            .hasNoLintViolations()
    }
}
