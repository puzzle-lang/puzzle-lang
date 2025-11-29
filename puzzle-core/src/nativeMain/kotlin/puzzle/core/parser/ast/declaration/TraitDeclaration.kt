package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.symbol.Modifier

@Serializable
class TraitDeclaration(
	val name: String,
	val modifiers: List<Modifier>,
	val typeSpec: TypeSpec?,
	val contextSpec: ContextSpec?,
	val members: List<Declaration>
) : Declaration