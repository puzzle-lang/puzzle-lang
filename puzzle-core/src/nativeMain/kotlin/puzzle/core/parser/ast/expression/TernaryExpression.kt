package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable

@Serializable
class TernaryExpression(
	val condition: Expression,
	val thenExpression: Expression,
	val elseExpression: Expression
) : Expression