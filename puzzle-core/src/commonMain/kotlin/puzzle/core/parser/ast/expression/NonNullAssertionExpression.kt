package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import puzzle.core.model.SourceLocation

@Serializable
class NonNullAssertionExpression(
	val receiver: Expression,
	override val location: SourceLocation,
) : Expression, CompoundAssignableProxy {
	
	@Transient
	override val inner = receiver
}