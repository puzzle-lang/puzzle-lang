package puzzle.core.parser.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.Modifier

@Serializable
data class ContractDeclaration(
	val name: String,
	val modifiers: Set<Modifier>,
	val members: List<Declaration> = emptyList(),
) : Declaration