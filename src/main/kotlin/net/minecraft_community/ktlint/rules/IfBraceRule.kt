package net.minecraft_community.ktlint.rules

import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleId

/**
 * TODO: Require braces on all if/else statements, even single-line bodies.
 *
 *   // Wrong
 *   if (bool) doThing()
 *
 *   // Correct
 *   if (bool) {
 *       doThing()
 *   }
 */
class IfBraceRule : Rule(
    ruleId = RuleId("libmcui-style:if-brace"),
    about = About(),
)
