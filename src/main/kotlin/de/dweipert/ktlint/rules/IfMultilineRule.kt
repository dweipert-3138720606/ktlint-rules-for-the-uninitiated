package de.dweipert.ktlint.rules

import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleId
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtIfExpression
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtReturnExpression
import org.jetbrains.kotlin.psi.KtValueArgument

class IfMultilineRule :
    Rule(
        ruleId = RuleId("uninitiated:if-multiline"),
        about = Rule.About(),
    ),
    Rule.OnlyWhenEnabledInEditorconfig {
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canAutoCorrect: Boolean) -> Unit,
    ) {
        if (node.psi is KtIfExpression && !node.text.contains("\n")) {
            if (!isStatementContext(node)) {
                return
            }
            emit(node.startOffset, "If statement must be multiline", false)
        }
    }

    private fun isStatementContext(node: ASTNode): Boolean {
        var parent = node.treeParent
        while (parent != null) {
            when (parent.psi) {
                is KtBlockExpression -> return true
                is KtProperty, is KtReturnExpression, is KtNamedFunction, is KtValueArgument -> return false
                else -> parent = parent.treeParent
            }
        }

        return true
    }
}
