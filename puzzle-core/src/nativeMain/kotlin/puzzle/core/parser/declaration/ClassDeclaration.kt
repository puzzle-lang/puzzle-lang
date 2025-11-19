package puzzle.core.parser.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.Modifier
import puzzle.core.parser.parameter.Parameter

@Serializable
data class ClassDeclaration(
	val name: String,
	val modifiers: Set<Modifier>,
	val constructorModifiers: Set<Modifier>,
	val parameters: List<Parameter>,
	val superTypes: List<SuperType>,
	val members: List<Declaration> = emptyList(),
) : Declaration