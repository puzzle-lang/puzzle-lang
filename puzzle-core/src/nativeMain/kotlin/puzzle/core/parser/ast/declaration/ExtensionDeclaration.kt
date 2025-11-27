package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.GenericSpec
import puzzle.core.parser.ast.node.TypeReference
import puzzle.core.symbol.Modifier

@Serializable
data class ExtensionDeclaration(
	val extendedType: TypeReference,
	val modifiers: List<Modifier>,
	val superTraits: List<SuperTrait>,
	val genericSpec: GenericSpec?,
	val contextSpec: ContextSpec?,
	val members: List<Declaration> = emptyList(),
) : Declaration