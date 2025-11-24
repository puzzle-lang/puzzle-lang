package puzzle.core.parser.ast.binding

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.TokenRange
import puzzle.core.parser.ast.node.TypeReference

@Serializable
data class GenericParameter(
	val name: String,
	val variance: Variance?,
	val bounds: List<TypeReference>,
	val defaultType: TypeReference?,
	val location: TokenRange
)

@Serializable
data class GenericSpec(
	val isReified: Boolean,
	val parameters: List<GenericParameter>,
	val location: TokenRange
)

enum class Variance {
	IN,
	OUT
}