package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable

@Serializable
data class NonNullAssertionExpression(
	val receiver: Expression
) : Expression