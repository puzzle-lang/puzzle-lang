package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.model.span

@Serializable
class TernaryExpression(
	val condition: Expression,
	val thenExpression: Expression,
	val elseExpression: Expression,
	override val location: SourceLocation = condition.location span elseExpression.location,
) : Expression