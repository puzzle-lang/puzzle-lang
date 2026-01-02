package puzzle.core.frontend.parser.ast.expression

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.copy

@Serializable
class GroupingExpression(
	val expression: Expression,
	override val location: SourceLocation = expression.location.copy(start = { it - 1 }, end = { it + 1 }),
) : Expression, CompoundAssignableProxy {
	
	@Transient
	override val inner = expression
}