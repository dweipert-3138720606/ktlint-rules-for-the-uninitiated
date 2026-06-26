package de.dweipert.ktlint.rules

import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleId
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.EditorConfig
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.EditorConfigProperty
import org.ec4j.core.model.PropertyType
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtNameReferenceExpression

val SKIP_ENUMS_PROPERTY: EditorConfigProperty<Boolean> =
    run({
        val type =
            PropertyType(
                "ktlint_uninitiated_no_primary_constructor_skip_enums",
                "Skip enum classes",
                PropertyType.PropertyValueParser.BOOLEAN_VALUE_PARSER,
            )
        val default = true
        EditorConfigProperty(
            type = type,
            defaultValue = default,
            ktlintOfficialCodeStyleDefaultValue = default,
            intellijIdeaCodeStyleDefaultValue = default,
            androidStudioCodeStyleDefaultValue = default,
            propertyMapper = { property, _ ->
                if (property == null || property.isUnset) {
                    default
                } else {
                    val parsed = type.parse(property.sourceValue)
                    if (parsed.isValid) {
                        parsed.parsed
                    } else {
                        default
                    }
                }
            },
            propertyWriter = { value -> value.toString() },
            name = "ktlint_uninitiated_no_primary_constructor_skip_enums",
        )
    })

class SecondaryConstructorRule :
    Rule(
        ruleId = RuleId("uninitiated:no-primary-constructor"),
        about = Rule.About(),
        usesEditorConfigProperties = setOf(SKIP_ENUMS_PROPERTY),
    ),
    Rule.OnlyWhenEnabledInEditorconfig {
    private var skipEnums = DEFAULT_SKIP_ENUMS

    override fun beforeFirstNode(editorConfig: EditorConfig) {
        skipEnums = editorConfig.get(SKIP_ENUMS_PROPERTY)
    }

    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canAutoCorrect: Boolean) -> Unit,
    ) {
        if (node.psi !is KtClass) {
            return
        }
        val klass = node.psi as KtClass

        if (klass.isInterface() || klass.isAnnotation() || klass.isData() || klass.isValue()) {
            return
        }
        if (skipEnums && klass.isEnum()) {
            return
        }
        if (klass.getPrimaryConstructorParameters().isEmpty()) {
            return
        }

        if (referencesInitBlock(klass)) {
            return
        }

        val paramList = klass.getPrimaryConstructorParameterList() ?: return
        emit(
            paramList.node.startOffset,
            "Class '${klass.name}' should use secondary constructors instead of a primary constructor",
            false,
        )
    }

    private fun referencesInitBlock(klass: KtClass): Boolean {
        val names =
            klass
                .getPrimaryConstructorParameters()
                .filter({ param -> param.hasValOrVar() })
                .mapNotNull({ param -> param.name })
                .toSet()
        if (names.isEmpty()) {
            return false
        }
        for (init in klass.getAnonymousInitializers()) {
            if (containsReference(init, names)) {
                return true
            }
        }

        return false
    }

    private fun containsReference(
        psi: PsiElement,
        names: Set<String>,
    ): Boolean {
        if (psi is KtNameReferenceExpression && psi.getReferencedName() in names) {
            return true
        }
        for (child in psi.children) {
            if (containsReference(child, names)) {
                return true
            }
        }

        return false
    }

    private companion object {
        const val DEFAULT_SKIP_ENUMS = true
    }
}
