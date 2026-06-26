package net.minecraft_community.ktlint.rules

import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleId
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.psi.KtCallExpression

class ParenthesesBeforeTrailingLambdaRule : Rule(
    ruleId = RuleId("uninitiated:parentheses-before-trailing-lambda"),
    about = Rule.About(),
), Rule.OnlyWhenEnabledInEditorconfig {
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canAutoCorrect: Boolean) -> Unit,
    ) {
        if (node.psi is KtCallExpression) {
            val call = node.psi as KtCallExpression
            if (call.valueArgumentList == null && call.lambdaArguments.isNotEmpty()) {
                emit(node.startOffset, "Parentheses are required before trailing lambda", false)
            }
        }
    }
}
