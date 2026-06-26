package de.dweipert.ktlint.rules

import com.pinterest.ktlint.rule.engine.core.api.RuleId
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.RuleExecution
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.createRuleExecutionEditorConfigProperty
import com.pinterest.ktlint.test.KtLintAssertThat
import org.junit.jupiter.api.Test

class SecondaryConstructorRuleTest {
    private val assertThat =
        KtLintAssertThat
            .assertThatRuleBuilder({ SecondaryConstructorRule() })
            .withEditorConfigOverride(
                Pair(
                    RuleId("uninitiated:no-primary-constructor")
                        .createRuleExecutionEditorConfigProperty(RuleExecution.enabled),
                    RuleExecution.enabled,
                ),
            ).assertThat()

    @Test
    fun `should flag class with primary constructor parameters`() {
        assertThat("class Foo(val x: Int)")
            .hasLintViolationWithoutAutoCorrect(
                1,
                10,
                "Class 'Foo' should use secondary constructors instead of a primary constructor",
            )
    }

    @Test
    fun `should flag class with non property parameters`() {
        assertThat("class Foo(x: Int)")
            .hasLintViolationWithoutAutoCorrect(
                1,
                10,
                "Class 'Foo' should use secondary constructors instead of a primary constructor",
            )
    }

    @Test
    fun `should not flag class without parameters`() {
        assertThat("class Foo")
            .hasNoLintViolations()
    }

    @Test
    fun `should not flag data class`() {
        assertThat("data class Foo(val x: Int)")
            .hasNoLintViolations()
    }

    @Test
    fun `should not flag enum class by default`() {
        assertThat("enum class Color(val rgb: Int) { RED(0xFF) }")
            .hasNoLintViolations()
    }

    @Test
    fun `should flag enum class when not skipped`() {
        val assertWithEnums =
            KtLintAssertThat
                .assertThatRuleBuilder({ SecondaryConstructorRule() })
                .withEditorConfigOverride(
                    Pair(
                        RuleId("uninitiated:no-primary-constructor")
                            .createRuleExecutionEditorConfigProperty(RuleExecution.enabled),
                        RuleExecution.enabled,
                    ),
                    Pair(SKIP_ENUMS_PROPERTY, false),
                ).assertThat()
        assertWithEnums("enum class Color(val rgb: Int) { RED(0xFF) }")
            .hasLintViolationWithoutAutoCorrect(
                1,
                17,
                "Class 'Color' should use secondary constructors instead of a primary constructor",
            )
    }

    @Test
    fun `should not flag annotation class`() {
        assertThat("annotation class Foo(val x: Int)")
            .hasNoLintViolations()
    }

    @Test
    fun `should not flag value class`() {
        assertThat("@JvmInline value class Foo(val x: Int)")
            .hasNoLintViolations()
    }

    @Test
    fun `should not flag interface`() {
        assertThat("interface Foo")
            .hasNoLintViolations()
    }

    @Test
    fun `should not flag object`() {
        assertThat("object Foo")
            .hasNoLintViolations()
    }

    @Test
    fun `should skip class when init block references constructor property`() {
        assertThat("class Foo(val x: Int) { init { println(x) } }")
            .hasNoLintViolations()
    }

    @Test
    fun `should skip class when init block references var constructor property`() {
        assertThat("class Foo(var x: Int) { init { x = 5 } }")
            .hasNoLintViolations()
    }

    @Test
    fun `should still flag class when init references non val-var parameter`() {
        assertThat("class Foo(x: Int) { init { println(x) } }")
            .hasLintViolationWithoutAutoCorrect(
                1,
                10,
                "Class 'Foo' should use secondary constructors instead of a primary constructor",
            )
    }

    @Test
    fun `should still flag class when init does not reference any constructor property`() {
        assertThat("class Foo(val x: Int) { init { println(\"hello\") } }")
            .hasLintViolationWithoutAutoCorrect(
                1,
                10,
                "Class 'Foo' should use secondary constructors instead of a primary constructor",
            )
    }

    @Test
    fun `should flag class with params used only in supertype delegation`() {
        assertThat(
            """
            open class Screen(
                title: Component = Component.literal(""),
            ) : MinecraftScreen(title) {
                lateinit var root: Container
                override fun init() {
                    super.init()
                    if (::root.isInitialized) {
                        root.width = width
                        root.height = height
                        root.layout()
                    }
                }
            }
            """.trimIndent(),
        ).hasLintViolationWithoutAutoCorrect(
            1,
            18,
            "Class 'Screen' should use secondary constructors instead of a primary constructor",
        )
    }
}
