package puzzle.core.parser.ast.binding

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.TypeReference
import puzzle.core.symbol.Modifier

@Serializable
data class Parameter(
	val name: String?,
	val modifiers: List<Modifier>,
	val typeReference: TypeReference,
	val defaultExpression: Expression? = null
)