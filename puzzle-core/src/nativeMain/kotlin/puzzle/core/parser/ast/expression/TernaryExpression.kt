package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable

/**
 * 三元表达式
 */
@Serializable
data class TernaryExpression(
	val condition: Expression,
	val thenExpression: Expression,
	val elseExpression: Expression
) : Expression