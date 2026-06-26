package net.minecraft_community.ktlint.rules

import com.pinterest.ktlint.rule.engine.core.api.RuleId
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.RuleExecution
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.createRuleExecutionEditorConfigProperty
import com.pinterest.ktlint.test.KtLintAssertThat
import org.junit.jupiter.api.Test

class IfBraceRuleTest {
    private val assertThat = KtLintAssertThat.assertThatRuleBuilder { IfBraceRule() }
        .withEditorConfigOverride(
            RuleId("uninitiated:if-brace").createRuleExecutionEditorConfigProperty(RuleExecution.enabled) to RuleExecution.enabled,
        )
        .assertThat()

    @Test
    fun `should flag if without braces`() {
        assertThat("fun f() { if (bool) doThing() }")
            .hasLintViolationWithoutAutoCorrect(1, 21, "If statement requires braces")
    }

    @Test
    fun `should not flag if with braces`() {
        assertThat("fun f() { if (bool) { doThing() } }")
            .hasNoLintViolations()
    }

    @Test
    fun `should flag else without braces`() {
        assertThat("fun f() { if (bool) { doThing() } else doOther() }")
            .hasLintViolationWithoutAutoCorrect(1, 40, "Else statement requires braces")
    }

    @Test
    fun `should flag else if without braces on body`() {
        assertThat("fun f() { if (bool) { doThing() } else if (other) doOther() }")
            .hasLintViolationWithoutAutoCorrect(1, 51, "If statement requires braces")
    }
}
