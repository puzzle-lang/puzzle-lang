package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.token.SourceLocation
import puzzle.core.token.copy

@Serializable
class GroupingExpression(
	val expression: Expression,
	override val location: SourceLocation =
		expression.location.copy(start = { it.start - 1 }, end = { it.end + 1 }),
) : Expression