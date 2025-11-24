package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.node.TypeReference
import puzzle.core.symbol.Modifier

@Serializable
data class ExtensionDeclaration(
	val extendedType: TypeReference,
	val modifiers: List<Modifier>,
	val superTraits: List<SuperTrait>,
	val members: List<Declaration> = emptyList(),
) : Declaration