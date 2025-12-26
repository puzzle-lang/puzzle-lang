package puzzle.core.parser.ast.argument

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.model.span
import puzzle.core.parser.ast.AstNode
import puzzle.core.parser.ast.type.TypeReference
import puzzle.core.parser.ast.expression.Identifier

@Serializable
class TypeArgument(
	val name: Identifier?,
	val type: TypeReference,
	override val location: SourceLocation =
		if (name != null) name.location span type.location else type.location,
) : AstNode