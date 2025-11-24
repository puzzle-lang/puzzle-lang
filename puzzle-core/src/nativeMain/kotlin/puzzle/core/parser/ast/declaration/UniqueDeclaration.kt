package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.symbol.Modifier

@Serializable
data class UniqueDeclaration(
	val name: String,
	val modifiers: List<Modifier>,
	val members: List<Declaration> = emptyList(),
) : Declaration