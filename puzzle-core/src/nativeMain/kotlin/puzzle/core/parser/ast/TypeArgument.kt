package puzzle.core.parser.ast

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.expression.Identifier
import puzzle.core.model.SourceLocation
import puzzle.core.model.span

@Serializable
class TypeArgument(
	val name: Identifier?,
	val type: TypeReference,
	override val location: SourceLocation =
		if (name != null) name.location span type.location else type.location,
) : AstNode