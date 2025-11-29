package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable

@Serializable
class NonNullAssertionExpression(
	val receiver: Expression
) : Expression