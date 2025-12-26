package puzzle.core.parser.ast.parameter

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.model.span
import puzzle.core.parser.ast.AstNode
import puzzle.core.parser.ast.type.TypeReference
import puzzle.core.parser.ast.expression.Identifier

@Serializable
class LambdaParameterReference(
	val name: Identifier,
	val type: TypeReference? = null,
	override val location: SourceLocation = if (type == null) name.location else name.location span type.location,
) : AstNode