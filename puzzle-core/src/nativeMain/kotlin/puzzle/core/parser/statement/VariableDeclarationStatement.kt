package puzzle.core.parser.statement

import kotlinx.serialization.Serializable
import puzzle.core.parser.expression.Expression
import puzzle.core.parser.node.TypeReference

@Serializable
data class VariableDeclarationStatement(
	val name: String,
	val initializer: Expression?,
	val isVariable: Boolean,
	val type: TypeReference?
) : Statement