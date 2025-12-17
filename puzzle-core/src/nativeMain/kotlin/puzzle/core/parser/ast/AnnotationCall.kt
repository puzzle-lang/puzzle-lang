package puzzle.core.parser.ast

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.expression.Argument
import puzzle.core.token.SourceLocation

@Serializable
class AnnotationCall(
	val type: TypeReference,
	override val location: SourceLocation,
	val arguments: List<Argument> = emptyList(),
) : AstNode