package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.TypeReference

@Serializable
class IsExpression(
	val expression: Expression,
	val type: TypeReference,
	val negated: Boolean
) : Expression