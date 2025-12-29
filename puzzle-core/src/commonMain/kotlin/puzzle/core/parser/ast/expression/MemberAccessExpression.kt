package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.model.span

@Serializable
class MemberAccessExpression(
	val receiver: Expression,
	val name: Expression,
	val isSafe: Boolean,
	override val location: SourceLocation = receiver.location span name.location,
) : Expression, DirectAssignable, CompoundAssignable