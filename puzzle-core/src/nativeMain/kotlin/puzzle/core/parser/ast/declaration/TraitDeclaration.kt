package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.GenericSpec
import puzzle.core.symbol.Modifier

@Serializable
data class TraitDeclaration(
	val name: String,
	val modifiers: List<Modifier>,
	val genericSpec: GenericSpec?,
	val contextSpec: ContextSpec?,
	val members: List<Declaration>
) : Declaration