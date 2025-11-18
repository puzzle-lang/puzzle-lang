package puzzle.core.parser.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.Modifier
import puzzle.core.parser.parameter.Parameter

@Serializable
data class StructDeclaration(
	val name: String,
	val modifiers: Set<Modifier>,
	val parameters: List<Parameter>
) : Declaration
