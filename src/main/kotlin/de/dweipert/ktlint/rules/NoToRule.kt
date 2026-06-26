package de.dweipert.ktlint.rules

import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleId
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.psi.KtBinaryExpression

class NoToRule :
    Rule(
        ruleId = RuleId("uninitiated:no-to"),
        about = Rule.About(),
    ),
    Rule.OnlyWhenEnabledInEditorconfig {
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canAutoCorrect: Boolean) -> Unit,
    ) {
        if (node.psi is KtBinaryExpression) {
            val expr = node.psi as KtBinaryExpression
            if (expr.operationReference.text == "to") {
                emit(
                    expr.operationReference.node.startOffset,
                    "Use 'Pair' instead of 'to'",
                    false,
                )
            }
        }
    }
}
