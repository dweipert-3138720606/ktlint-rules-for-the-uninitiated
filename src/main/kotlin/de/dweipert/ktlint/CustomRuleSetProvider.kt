package de.dweipert.ktlint

import com.pinterest.ktlint.cli.ruleset.core.api.RuleSetProviderV3
import com.pinterest.ktlint.rule.engine.core.api.RuleProvider
import com.pinterest.ktlint.rule.engine.core.api.RuleSetId
import de.dweipert.ktlint.rules.BlankLineBeforeReturnRule
import de.dweipert.ktlint.rules.ExplicitLambdaParamRule
import de.dweipert.ktlint.rules.IfBraceRule
import de.dweipert.ktlint.rules.IfMultilineRule
import de.dweipert.ktlint.rules.ParenthesesBeforeTrailingLambdaRule
import de.dweipert.ktlint.rules.SecondaryConstructorRule
import de.dweipert.ktlint.rules.VariableNameLengthRule

class CustomRuleSetProvider : RuleSetProviderV3(RuleSetId("uninitiated")) {
    override fun getRuleProviders(): Set<RuleProvider> =
        setOf(
            RuleProvider({ ExplicitLambdaParamRule() }),
            RuleProvider({ IfBraceRule() }),
            RuleProvider({ IfMultilineRule() }),
            RuleProvider({ ParenthesesBeforeTrailingLambdaRule() }),
            RuleProvider({ BlankLineBeforeReturnRule() }),
            RuleProvider({ SecondaryConstructorRule() }),
            RuleProvider({ VariableNameLengthRule() }),
        )
}
