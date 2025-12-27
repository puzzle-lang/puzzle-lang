package puzzle.core.parser.ast

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.expression.Argument
import puzzle.core.parser.ast.type.NamedType

@Serializable
class AnnotationCall(
	val type: NamedType,
	override val location: SourceLocation,
	val arguments: List<Argument> = emptyList(),
) : AstNode