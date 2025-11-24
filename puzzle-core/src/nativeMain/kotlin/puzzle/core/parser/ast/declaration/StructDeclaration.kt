package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.binding.Parameter
import puzzle.core.symbol.Modifier

@Serializable
data class StructDeclaration(
	val name: String,
	val modifiers: List<Modifier>,
	val parameters: List<Parameter>
) : Declaration
