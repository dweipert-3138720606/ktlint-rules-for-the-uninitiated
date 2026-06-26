package de.dweipert.ktlint.rules

import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleId
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtLambdaExpression
import org.jetbrains.kotlin.psi.KtNameReferenceExpression

class ExplicitLambdaParamRule :
    Rule(
        ruleId = RuleId("uninitiated:explicit-lambda-param"),
        about = Rule.About(),
    ),
    Rule.OnlyWhenEnabledInEditorconfig {
    @Suppress("OVERRIDE_DEPRECATION")
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canAutoCorrect: Boolean) -> Unit,
    ) {
        if (node.psi is KtLambdaExpression) {
            val lambda = node.psi as KtLambdaExpression
            if (lambda.valueParameters.isEmpty()) {
                val body = lambda.bodyExpression ?: return
                if (!bodyUsesImplicitIt(body)) {
                    return
                }
                emit(node.startOffset, "Lambda must have explicit parameter names instead of implicit 'it'", false)
            }
        }
    }

    private fun bodyUsesImplicitIt(body: KtBlockExpression): Boolean {
        val refs = PsiTreeUtil.collectElementsOfType(body, KtNameReferenceExpression::class.java)

        outer@ for (ref in refs) {
            if (ref.getReferencedName() != "it") {
                continue@outer
            }
            var parent = ref.parent
            while (parent != null && parent != body) {
                if (parent is KtLambdaExpression) {
                    continue@outer
                }
                parent = parent.parent
            }

            return true
        }

        return false
    }
}
