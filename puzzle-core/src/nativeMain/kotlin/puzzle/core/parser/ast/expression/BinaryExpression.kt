package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.symbol.Operator

@Serializable
data class BinaryExpression(
	val left: Expression,
	val operator: Operator,
	val right: Expression,
) : Expression