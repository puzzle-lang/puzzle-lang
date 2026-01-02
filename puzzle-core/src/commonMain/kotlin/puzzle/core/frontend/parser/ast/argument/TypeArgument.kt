package puzzle.core.frontend.parser.ast.argument

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.ast.AstNode
import puzzle.core.frontend.parser.ast.type.TypeReference
import puzzle.core.frontend.parser.ast.expression.Identifier

@Serializable
class TypeArgument(
	val name: Identifier?,
	val type: TypeReference,
	override val location: SourceLocation =
		if (name != null) name.location span type.location else type.location,
) : AstNode