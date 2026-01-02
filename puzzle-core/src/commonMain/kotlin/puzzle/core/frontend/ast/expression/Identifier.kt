package puzzle.core.frontend.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation

@Serializable
class Identifier(
	val name: String,
	override val location: SourceLocation,
) : Expression, DirectAssignable, CompoundAssignable