package puzzle.core.parser.binding

import kotlinx.serialization.Serializable
import puzzle.core.parser.node.TypeReference

@Serializable
data class GenericParameter(
	val name: String,
	val bound: TypeReference? = null,
	val defaultType: TypeReference? = null,
	val strictBound: Boolean = false,
	val variance: Variance? = null
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