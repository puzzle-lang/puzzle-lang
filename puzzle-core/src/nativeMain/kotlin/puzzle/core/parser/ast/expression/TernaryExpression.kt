package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.token.SourceLocation
import puzzle.core.token.span

@Serializable
class TernaryExpression(
	val condition: Expression,
	val thenExpression: Expression,
	val elseExpression: Expression,
	override val location: SourceLocation = condition.location span elseExpression.location,
) : Expression