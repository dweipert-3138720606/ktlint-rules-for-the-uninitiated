package net.minecraft_community.ktlint.rules

import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleId
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtIfExpression

class IfBraceRule : Rule(
    ruleId = RuleId("uninitiated:if-brace"),
    about = Rule.About(),
), Rule.OnlyWhenEnabledInEditorconfig {
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canAutoCorrect: Boolean) -> Unit,
    ) {
        if (node.psi is KtIfExpression) {
            val ifExpr = node.psi as KtIfExpression
            if (ifExpr.then != null && ifExpr.then !is KtBlockExpression) {
                emit(ifExpr.then!!.node.startOffset, "If statement requires braces", false)
            }
            val elseBody = ifExpr.`else`
            if (elseBody != null && elseBody !is KtBlockExpression && elseBody !is KtIfExpression) {
                emit(elseBody.node.startOffset, "Else statement requires braces", false)
            }
        }
    }
}
