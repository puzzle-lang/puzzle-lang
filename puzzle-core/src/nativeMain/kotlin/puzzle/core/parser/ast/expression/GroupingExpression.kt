package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable

@Serializable
class GroupingExpression(
	val expression: Expression
) : Expression