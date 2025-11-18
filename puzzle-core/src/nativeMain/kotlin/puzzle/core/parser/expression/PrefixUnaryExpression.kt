package puzzle.core.parser.expression

import kotlinx.serialization.Serializable
import puzzle.core.parser.Operator

@Serializable
data class PrefixUnaryExpression(
	val operator: Operator,
	val expression: Expression
) : Expression