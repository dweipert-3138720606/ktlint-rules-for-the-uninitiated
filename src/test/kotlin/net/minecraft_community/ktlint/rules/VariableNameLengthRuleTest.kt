package net.minecraft_community.ktlint.rules

import com.pinterest.ktlint.rule.engine.core.api.RuleId
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.RuleExecution
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.createRuleExecutionEditorConfigProperty
import com.pinterest.ktlint.test.KtLintAssertThat
import com.pinterest.ktlint.test.LintViolation
import org.junit.jupiter.api.Test

class VariableNameLengthRuleTest {
    private val assertThat = KtLintAssertThat.assertThatRuleBuilder { VariableNameLengthRule() }
        .withEditorConfigOverride(
            RuleId("uninitiated:variable-name-length").createRuleExecutionEditorConfigProperty(RuleExecution.enabled) to RuleExecution.enabled,
        )
        .assertThat()

    @Test
    fun `should flag short local variable`() {
        assertThat("fun f() { val a = 1 }")
            .hasLintViolationWithoutAutoCorrect(1, 15, "Variable name 'a' is too short (min 3 chars)")
    }

    @Test
    fun `should not flag long enough variable`() {
        assertThat("fun f() { val abc = 1 }")
            .hasNoLintViolations()
    }

    @Test
    fun `should skip underscore`() {
        assertThat("fun f() { val _ = 1 }")
            .hasNoLintViolations()
    }

    @Test
    fun `should skip it`() {
        assertThat("fun f() { listOf(1).forEach { val it = 1 } }")
            .hasNoLintViolations()
    }

    @Test
    fun `should flag short parameter name`() {
        assertThat("fun f(x: Int) {}")
            .hasLintViolationWithoutAutoCorrect(1, 7, "Variable name 'x' is too short (min 3 chars)")
    }

    @Test
    fun `should not flag long parameter name`() {
        assertThat("fun f(param: Int) {}")
            .hasNoLintViolations()
    }

    @Test
    fun `should flag short destructured variable`() {
        assertThat("fun f() { val (a, b) = Pair(1, 2) }")
            .hasLintViolationsWithoutAutoCorrect(
                LintViolation(1, 16, "Variable name 'a' is too short (min 3 chars)"),
                LintViolation(1, 19, "Variable name 'b' is too short (min 3 chars)"),
            )
    }

    @Test
    fun `should flag short class property`() {
        assertThat("class C { val a = 1 }")
            .hasLintViolationWithoutAutoCorrect(1, 15, "Variable name 'a' is too short (min 3 chars)")
    }

    @Test
    fun `should respect custom min length`() {
        assertThat("fun f() { val abcd = 1 }")
            .withEditorConfigOverride(MIN_VARIABLE_NAME_LENGTH_PROPERTY to 5)
            .hasLintViolationWithoutAutoCorrect(1, 15, "Variable name 'abcd' is too short (min 5 chars)")
    }

    @Test
    fun `should accept custom skip names`() {
        assertThat("fun f(x: Int) { val ab = 1 }")
            .withEditorConfigOverride(SKIP_VARIABLE_NAMES_PROPERTY to "_, it, x")
            .hasLintViolationWithoutAutoCorrect(1, 21, "Variable name 'ab' is too short (min 3 chars)")
    }

    @Test
    fun `should skip custom names`() {
        assertThat("fun f() { val ab = 1 }")
            .withEditorConfigOverride(SKIP_VARIABLE_NAMES_PROPERTY to "ab")
            .hasNoLintViolations()
    }

    @Test
    fun `should flag short variable with empty skip list`() {
        assertThat("fun f() { val _ = 1 }")
            .withEditorConfigOverride(SKIP_VARIABLE_NAMES_PROPERTY to "")
            .hasLintViolationWithoutAutoCorrect(1, 15, "Variable name '_' is too short (min 3 chars)")
    }
}
