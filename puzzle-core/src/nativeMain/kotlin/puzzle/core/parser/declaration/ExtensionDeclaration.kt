package puzzle.core.parser.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.Modifier
import puzzle.core.parser.node.TypeReference

@Serializable
data class ExtensionDeclaration(
	val extendedType: TypeReference,
	val modifiers: Set<Modifier>,
	val superTraits: List<SuperTrait>,
	val members: List<Declaration> = emptyList(),
) : Declaration