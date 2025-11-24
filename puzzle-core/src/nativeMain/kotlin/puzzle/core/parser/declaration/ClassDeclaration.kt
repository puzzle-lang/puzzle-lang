package puzzle.core.parser.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.Modifier
import puzzle.core.parser.binding.parameter.Parameter

@Serializable
data class ClassDeclaration(
	val name: String,
	val modifiers: List<Modifier>,
	val constructorModifiers: List<Modifier>,
	val parameters: List<Parameter>,
	val superTypes: List<SuperType>,
	val members: List<Declaration> = emptyList(),
) : Declaration