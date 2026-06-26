package net.minecraft_community.ktlint

import com.pinterest.ktlint.cli.ruleset.core.api.RuleSetProviderV3
import com.pinterest.ktlint.rule.engine.core.api.RuleProvider
import com.pinterest.ktlint.rule.engine.core.api.RuleSetId
import net.minecraft_community.ktlint.rules.BlankLineBeforeReturnRule
import net.minecraft_community.ktlint.rules.ExplicitLambdaParamRule
import net.minecraft_community.ktlint.rules.IfBraceRule
import net.minecraft_community.ktlint.rules.IfMultilineRule
import net.minecraft_community.ktlint.rules.ParenthesesBeforeTrailingLambdaRule
import net.minecraft_community.ktlint.rules.VariableNameLengthRule

class CustomRuleSetProvider : RuleSetProviderV3(RuleSetId("uninitiated")) {
    override fun getRuleProviders(): Set<RuleProvider> =
        setOf(
            RuleProvider({ ExplicitLambdaParamRule() }),
            RuleProvider({ ParenthesesBeforeTrailingLambdaRule() }),
            RuleProvider({ IfBraceRule() }),
            RuleProvider({ IfMultilineRule() }),
            RuleProvider({ BlankLineBeforeReturnRule() }),
            RuleProvider({ VariableNameLengthRule() }),
        )
}
