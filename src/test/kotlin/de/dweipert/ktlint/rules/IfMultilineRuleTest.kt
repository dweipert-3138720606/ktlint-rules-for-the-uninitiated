package de.dweipert.ktlint.rules

import com.pinterest.ktlint.rule.engine.core.api.RuleId
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.RuleExecution
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.createRuleExecutionEditorConfigProperty
import com.pinterest.ktlint.test.KtLintAssertThat
import com.pinterest.ktlint.test.LintViolation
import org.junit.jupiter.api.Test

class IfMultilineRuleTest {
    private val assertThat =
        KtLintAssertThat
            .assertThatRuleBuilder({ IfMultilineRule() })
            .withEditorConfigOverride(
                RuleId("uninitiated:if-multiline").createRuleExecutionEditorConfigProperty(RuleExecution.enabled) to RuleExecution.enabled,
            ).assertThat()

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
        val code =
            """
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
        val code =
            """
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
    fun `should not flag multiline else-if with braces`() {
        val code =
            """
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

    @Test
    fun `should not flag inline if in variable assignment`() {
        assertThat("fun f() { val x = if (b) \"a\" else \"b\" }")
            .hasNoLintViolations()
    }

    @Test
    fun `should not flag inline if in return expression`() {
        assertThat("fun f() = if (b) \"a\" else \"b\"")
            .hasNoLintViolations()
    }

    @Test
    fun `should not flag inline if as function argument`() {
        assertThat("fun f() { foo(if (b) \"a\" else \"b\") }")
            .hasNoLintViolations()
    }

    @Test
    fun `should still flag else-if chain`() {
        assertThat("fun f() { if (x) { a() } else if (y) { b() } }")
            .hasLintViolations(
                LintViolation(1, 11, "If statement must be multiline", false),
                LintViolation(1, 31, "If statement must be multiline", false),
            )
    }
}
