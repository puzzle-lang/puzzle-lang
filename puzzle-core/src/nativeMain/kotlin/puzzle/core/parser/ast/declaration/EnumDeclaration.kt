package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.binding.Parameter
import puzzle.core.symbol.Modifier

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