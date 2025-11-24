package puzzle.core.parser.ast.binding

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.node.TypeReference

@Serializable
data class GenericParameter(
	val name: String,
	val variance: Variance?,
	val bounds: List<TypeReference>,
	val defaultType: TypeReference?,
)

@Serializable
data class GenericSpec(
	val isReified: Boolean,
	val parameters: List<GenericParameter>
)

enum class Variance {
	IN,
	OUT
}