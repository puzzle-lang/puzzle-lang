package puzzle.core.parser.ast.type

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.AstNode

@Serializable
class TypeReference(
	val type: Type,
	val isNullable: Boolean,
	override val location: SourceLocation,
) : AstNode