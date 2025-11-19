package puzzle.core.parser.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.expression.Argument
import puzzle.core.parser.node.TypeReference

@Serializable
sealed interface SuperType {
	val type: TypeReference
}

@Serializable
data class SuperClass(
	override val type: TypeReference,
	val arguments: List<Argument> = emptyList(),
) : SuperType

/**
 * HelloWorld
 */
@Serializable
data class SuperTrait(
	override val type: TypeReference,
) : SuperType