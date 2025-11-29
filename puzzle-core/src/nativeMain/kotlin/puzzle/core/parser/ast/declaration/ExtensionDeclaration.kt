package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.TypeReference
import puzzle.core.symbol.Modifier

@Serializable
class ExtensionDeclaration(
	val extendedType: TypeReference,
	val modifiers: List<Modifier>,
	val superTraits: List<SuperTrait>,
	val typeSpec: TypeSpec?,
	val contextSpec: ContextSpec?,
	val members: List<Declaration> = emptyList(),
) : Declaration