package puzzle.core.parser.binding.parameter

import kotlinx.serialization.Serializable
import puzzle.core.parser.Modifier
import puzzle.core.parser.expression.Expression
import puzzle.core.parser.node.TypeReference

@Serializable
data class Parameter(
	val name: String?,
	val modifiers: List<Modifier>,
	val typeReference: TypeReference,
	val defaultExpression: Expression? = null
)