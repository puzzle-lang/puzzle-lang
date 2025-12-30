package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import puzzle.core.model.SourceLocation
import puzzle.core.model.copy

@Serializable
class GroupingExpression(
	val expression: Expression,
	override val location: SourceLocation = expression.location.copy(start = { it - 1 }, end = { it + 1 }),
) : Expression, CompoundAssignableProxy {
	
	@Transient
	override val inner = expression
}