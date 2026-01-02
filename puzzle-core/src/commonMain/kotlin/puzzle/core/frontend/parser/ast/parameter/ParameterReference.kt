package puzzle.core.frontend.parser.ast.parameter

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.ast.AstNode
import puzzle.core.frontend.parser.ast.type.TypeReference
import puzzle.core.frontend.parser.ast.expression.Identifier

@Serializable
class ParameterReference(
	val name: Identifier,
	val type: TypeReference? = null,
	override val location: SourceLocation = if (type == null) name.location else name.location span type.location,
) : AstNode