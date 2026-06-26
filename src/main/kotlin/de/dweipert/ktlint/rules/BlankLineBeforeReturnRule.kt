package de.dweipert.ktlint.rules

import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleId
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtReturnExpression

class BlankLineBeforeReturnRule :
    Rule(
        ruleId = RuleId("uninitiated:blank-line-before-return"),
        about = Rule.About(),
    ),
    Rule.OnlyWhenEnabledInEditorconfig {
    @Suppress("OVERRIDE_DEPRECATION")
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canAutoCorrect: Boolean) -> Unit,
    ) {
        if (node.psi is KtBlockExpression) {
            val block = node.psi as KtBlockExpression
            val statements = block.statements
            if (statements.size > 1 && statements.last() is KtReturnExpression) {
                val returnNode = statements.last().node
                val prevSibling = returnNode.treePrev
                if (prevSibling == null || !prevSibling.text.contains("\n\n")) {
                    emit(returnNode.startOffset, "Add a blank line before the return statement", false)
                }
            }
        }
    }
}
