package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.expression.Argument
import puzzle.core.parser.ast.TypeReference

@Serializable
sealed interface SuperType {
	val type: TypeReference
}

@Serializable
class SuperClass(
	override val type: TypeReference,
	val arguments: List<Argument> = emptyList(),
) : SuperType

@Serializable
class SuperTrait(
	override val type: TypeReference,
) : SuperType