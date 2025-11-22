package puzzle.core.parser.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.Modifier

@Serializable
data class SingleDeclaration(
	val name: String,
	val modifiers: List<Modifier>,
	val members: List<Declaration> = emptyList(),
) : Declaration