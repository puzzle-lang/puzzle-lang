package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.GenericSpec
import puzzle.core.parser.ast.binding.Parameter
import puzzle.core.symbol.Modifier

@Serializable
data class EnumDeclaration(
	val name: String,
	val modifiers: List<Modifier>,
	val parameters: List<Parameter>,
	val entries: List<EnumEntry>,
	val genericSpec: GenericSpec?,
	val contextSpec: ContextSpec?,
	val members: List<Declaration>
) : Declaration

@Serializable
data class EnumEntry(
	val name: String,
	val members: List<Declaration>,
)