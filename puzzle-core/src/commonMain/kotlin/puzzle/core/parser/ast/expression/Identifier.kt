package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation

@Serializable
class Identifier(
	val name: String,
	override val location: SourceLocation,
) : Expression, DirectAssignable, CompoundAssignable