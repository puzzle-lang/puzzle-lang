package puzzle.core.parser.expression

import kotlinx.serialization.Serializable

@Serializable
data class NonNullAssertionExpression(
	val receiver: Expression
) : Expression