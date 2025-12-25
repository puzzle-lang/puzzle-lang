package puzzle.core.parser.ast

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation

@Serializable
class DocComment(
	val value: String,
	override val location: SourceLocation,
) : AstNode