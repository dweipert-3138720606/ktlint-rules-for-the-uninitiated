package net.minecraft_community.ktlint.rules

import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleId

/**
 * TODO: Enforce parentheses before trailing lambda.
 *
 * When the last argument of a function is a lambda, Kotlin allows omitting the parentheses
 * and placing the lambda outside. This rule should require keeping the parentheses.
 *
 *   // Wrong
 *   list.count { it > 0 }
 *
 *   // Correct
 *   list.count({ it > 0 })
 */
class ParenthesesBeforeTrailingLambdaRule : Rule(
    ruleId = RuleId("libmcui-style:parentheses-before-trailing-lambda"),
    about = About(),
)
