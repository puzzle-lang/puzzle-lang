package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.symbol.Operator

@Serializable
data class PrefixUnaryExpression(
	val operator: Operator,
	val expression: Expression
) : Expression