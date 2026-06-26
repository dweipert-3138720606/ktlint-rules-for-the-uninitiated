package net.minecraft_community.ktlint.rules

import com.pinterest.ktlint.rule.engine.core.api.RuleId
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.RuleExecution
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.createRuleExecutionEditorConfigProperty
import com.pinterest.ktlint.test.KtLintAssertThat
import org.junit.jupiter.api.Test

class ExplicitLambdaParamRuleTest {
    private val assertThat =
        KtLintAssertThat
            .assertThatRuleBuilder({ ExplicitLambdaParamRule() })
            .withEditorConfigOverride(
                RuleId("uninitiated:explicit-lambda-param").createRuleExecutionEditorConfigProperty(RuleExecution.enabled) to
                    RuleExecution.enabled,
            ).assertThat()

    @Test
    fun `should flag implicit it`() {
        assertThat("fun f() { list.map { it.toString() } }")
            .hasLintViolationWithoutAutoCorrect(1, 20, "Lambda must have explicit parameter names instead of implicit 'it'")
    }

    @Test
    fun `should not flag explicit parameter name`() {
        assertThat("fun f() { list.map { item -> item.toString() } }")
            .hasNoLintViolations()
    }

    @Test
    fun `should not flag lambda with no parameters and no it reference`() {
        assertThat("fun f() { val f = { 42 } }")
            .hasNoLintViolations()
    }
}
