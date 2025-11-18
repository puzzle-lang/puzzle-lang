package puzzle.core.parser.expression

import kotlinx.serialization.Serializable
import puzzle.core.parser.Operator

@Serializable
data class SuffixUnaryExpression(
	val expression: Expression,
	val operator: Operator
) : Expression