package puzzle.core.parser.binding.generic

import kotlinx.serialization.Serializable
import puzzle.core.parser.node.TypeReference

@Serializable
data class GenericParameter(
	val name: String,
	val variance: Variance?,
	val bounds: List<TypeReference>,
	val defaultType: TypeReference?,
)

@Serializable
data class GenericDefinition(
	val isReified: Boolean,
	val parameters: List<GenericParameter>
)

enum class Variance {
	IN,
	OUT
}