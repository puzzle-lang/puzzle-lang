package puzzle.core.parser.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.Modifier
import puzzle.core.parser.binding.Parameter

@Serializable
data class EnumDeclaration(
	val name: String,
	val modifiers: List<Modifier>,
	val parameters: List<Parameter>,
	val entries: List<EnumEntry>,
	val members: List<Declaration> = emptyList(),
) : Declaration

@Serializable
data class EnumEntry(
	val name: String,
	val members: List<Declaration>,
)