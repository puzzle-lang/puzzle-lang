package puzzle.core.parser.expression

import kotlinx.serialization.Serializable
import puzzle.core.parser.Operator

@Serializable
data class BinaryExpression(
	val left: Expression,
	val operator: Operator,
	val right: Expression,
) : Expression