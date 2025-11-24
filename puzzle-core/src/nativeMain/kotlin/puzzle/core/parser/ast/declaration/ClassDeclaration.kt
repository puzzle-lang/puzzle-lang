package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.binding.Parameter
import puzzle.core.symbol.Modifier

@Serializable
data class ClassDeclaration(
	val name: String,
	val modifiers: List<Modifier>,
	val constructorModifiers: List<Modifier>,
	val parameters: List<Parameter>,
	val superTypes: List<SuperType>,
	val members: List<Declaration> = emptyList(),
) : Declaration