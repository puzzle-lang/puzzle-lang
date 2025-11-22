package puzzle.core.parser.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.Modifier
import puzzle.core.parser.binding.Parameter

@Serializable
data class AnnotationDeclaration(
	val name: String,
	val modifiers: List<Modifier>,
	val parameters: List<Parameter> = emptyList(),
) : Declaration