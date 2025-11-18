package puzzle.core.parser.declaration

import puzzle.core.parser.Modifier
import puzzle.core.parser.parameter.Parameter
import kotlinx.serialization.Serializable

@Serializable
data class ClassDeclaration(
	val name: String,
	val modifiers: Set<Modifier>,
	val constructorModifiers: Set<Modifier>,
	val parameters: List<Parameter>,
	val members: List<Declaration> = emptyList(),
) : Declaration