package net.minecraft_community.ktlint.rules

import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleId
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.psi.KtIfExpression

class IfMultilineRule : Rule(
    ruleId = RuleId("uninitiated:if-multiline"),
    about = Rule.About(),
), Rule.OnlyWhenEnabledInEditorconfig {
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canAutoCorrect: Boolean) -> Unit,
    ) {
        if (node.psi is KtIfExpression && !node.text.contains("\n")) {
            emit(node.startOffset, "If statement must be multiline", false)
        }
    }
}
