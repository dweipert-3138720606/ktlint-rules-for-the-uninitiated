package net.minecraft_community.ktlint.rules

import com.pinterest.ktlint.rule.engine.core.api.RuleId
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.RuleExecution
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.createRuleExecutionEditorConfigProperty
import com.pinterest.ktlint.test.KtLintAssertThat
import org.junit.jupiter.api.Test

class ParenthesesBeforeTrailingLambdaRuleTest {
    private val assertThat = KtLintAssertThat.assertThatRuleBuilder { ParenthesesBeforeTrailingLambdaRule() }
        .withEditorConfigOverride(
            RuleId("uninitiated:parentheses-before-trailing-lambda").createRuleExecutionEditorConfigProperty(RuleExecution.enabled) to RuleExecution.enabled,
        )
        .assertThat()

    @Test
    fun `should flag trailing lambda without parentheses`() {
        assertThat("fun f() { list.count { it > 0 } }")
            .hasLintViolationWithoutAutoCorrect(1, 16, "Parentheses are required before trailing lambda")
    }

    @Test
    fun `should not flag call with parentheses`() {
        assertThat("fun f() { list.count({ it > 0 }) }")
            .hasNoLintViolations()
    }

    @Test
    fun `should not flag call with no lambda`() {
        assertThat("fun f() { list.count() }")
            .hasNoLintViolations()
    }
}
