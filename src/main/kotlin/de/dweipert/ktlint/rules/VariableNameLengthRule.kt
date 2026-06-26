package de.dweipert.ktlint.rules

import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleId
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.EditorConfig
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.EditorConfigProperty
import org.ec4j.core.model.PropertyType
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.psi.KtDestructuringDeclarationEntry
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.KtProperty

val MIN_VARIABLE_NAME_LENGTH_PROPERTY: EditorConfigProperty<Int> =
    run({
        val type =
            PropertyType(
                "ktlint_uninitiated_min_variable_name_length",
                "Minimum variable name length",
                PropertyType.PropertyValueParser.POSITIVE_INT_VALUE_PARSER,
            )
        val default = 3
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
            name = "ktlint_uninitiated_min_variable_name_length",
        )
    })

val SKIP_VARIABLE_NAMES_PROPERTY: EditorConfigProperty<String> =
    run({
        val type =
            PropertyType(
                "ktlint_uninitiated_min_variable_name_skip_names",
                "Variable names to skip",
                PropertyType.PropertyValueParser.IDENTITY_VALUE_PARSER,
            )
        val default = "_, it"
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
                    property.sourceValue
                }
            },
            propertyWriter = { value -> value },
            name = "ktlint_uninitiated_min_variable_name_skip_names",
        )
    })

class VariableNameLengthRule :
    Rule(
        ruleId = RuleId("uninitiated:variable-name-length"),
        about = Rule.About(),
        usesEditorConfigProperties = setOf(MIN_VARIABLE_NAME_LENGTH_PROPERTY, SKIP_VARIABLE_NAMES_PROPERTY),
    ),
    Rule.OnlyWhenEnabledInEditorconfig {
    private var minLength = DEFAULT_MIN_LENGTH
    private var skipNames = DEFAULT_SKIP_NAMES

    override fun beforeFirstNode(editorConfig: EditorConfig) {
        minLength = editorConfig.get(MIN_VARIABLE_NAME_LENGTH_PROPERTY)
        skipNames =
            editorConfig
                .get(SKIP_VARIABLE_NAMES_PROPERTY)
                .split(",")
                .map({ name -> name.trim() })
                .filter({ name -> name.isNotEmpty() })
                .toSet()
    }

    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canAutoCorrect: Boolean) -> Unit,
    ) {
        val psi = node.psi
        val name: String?
        val nameOffset: Int
        when (psi) {
            is KtProperty -> {
                name = psi.name
                nameOffset = psi.nameIdentifier?.node?.startOffset ?: psi.node.startOffset
            }

            is KtParameter -> {
                name = psi.name
                nameOffset = psi.nameIdentifier?.node?.startOffset ?: psi.node.startOffset
            }

            is KtDestructuringDeclarationEntry -> {
                name = psi.name
                nameOffset = psi.nameIdentifier?.node?.startOffset ?: psi.node.startOffset
            }

            else -> {
                return
            }
        }
        if (name == null) {
            return
        }
        if (name in skipNames) {
            return
        }
        if (name.length >= minLength) {
            return
        }
        emit(nameOffset, "Variable name '$name' is too short (min $minLength chars)", false)
    }

    private companion object {
        const val DEFAULT_MIN_LENGTH = 3
        val DEFAULT_SKIP_NAMES = setOf("_", "it")
    }
}
