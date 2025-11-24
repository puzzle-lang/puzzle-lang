package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.symbol.Operator

@Serializable
data class SuffixUnaryExpression(
	val expression: Expression,
	val operator: Operator
) : Expression