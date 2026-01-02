package puzzle.core.frontend.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.span

@Serializable
class TernaryExpression(
	val condition: Expression,
	val thenExpression: Expression,
	val elseExpression: Expression,
	override val location: SourceLocation = condition.location span elseExpression.location,
) : Expression, CompoundAssignable