package puzzle.core.frontend.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.span
import puzzle.core.frontend.ast.type.TypeReference

@Serializable
class AsExpression(
	val expression: Expression,
	val type: TypeReference,
	val isSafe: Boolean,
	override val location: SourceLocation = expression.location span type.location,
) : Expression, CompoundAssignable