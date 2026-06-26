package net.minecraft_community.ktlint.rules

import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleId

/**
 * TODO: Forbid implicit `it` in lambda expressions.
 *
 * When a lambda has a single parameter, Kotlin allows using the implicit `it` name instead
 * of declaring an explicit parameter. This rule should require explicit parameter names.
 *
 *   // Wrong
 *   list.map { it.toString() }
 *
 *   // Correct
 *   list.map { item -> item.toString() }
 */
class ExplicitLambdaParamRule : Rule(
    ruleId = RuleId("libmcui-style:explicit-lambda-param"),
    about = About(),
)
