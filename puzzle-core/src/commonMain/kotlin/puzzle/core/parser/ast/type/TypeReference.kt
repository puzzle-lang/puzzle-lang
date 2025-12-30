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

fun TypeReference.copy(
	type: Type = this.type,
	isNullable: Boolean = this.isNullable,
	location: SourceLocation = this.location,
): TypeReference = TypeReference(type, isNullable, location)