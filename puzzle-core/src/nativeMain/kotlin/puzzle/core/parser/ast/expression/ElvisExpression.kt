package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable

@Serializable
data class ElvisExpression(
	val left: Expression,
	val right: Expression
) : Expression