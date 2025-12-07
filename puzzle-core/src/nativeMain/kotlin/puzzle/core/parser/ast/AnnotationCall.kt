package puzzle.core.parser.ast

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.expression.Argument

@Serializable
class AnnotationCall(
	val type: TypeReference,
	val arguments: List<Argument> = emptyList()
)