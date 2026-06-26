package net.minecraft_community.ktlint.rules

import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleId

/**
 * TODO: Require a blank line before the last return statement when the function body
 * has more than one statement.
 *
 *   // Wrong
 *   fun example(value: Int): String {
 *       val result = doSomething(value)
 *       val transformed = transform(result)
 *       return transformed
 *   }
 *
 *   // Correct
 *   fun example(value: Int): String {
 *       val result = doSomething(value)
 *       val transformed = transform(result)
 *
 *       return transformed
 *   }
 *
 *   // Also fine (single statement, no blank line needed)
 *   fun simple(value: Int): String {
 *       return value.toString()
 *   }
 */
class BlankLineBeforeReturnRule : Rule(
    ruleId = RuleId("libmcui-style:blank-line-before-return"),
    about = About(),
)
