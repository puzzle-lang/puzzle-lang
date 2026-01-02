package puzzle.core.frontend.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation

@Serializable
class MemberReferenceExpression(
	val receiver: Expression?,
	val name: Expression,
	override val location: SourceLocation,
) : Expression, CompoundAssignable