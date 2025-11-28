package puzzle.core.parser.ast.binding

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.TokenRange
import puzzle.core.parser.ast.TypeReference

@Serializable
data class TypeParameter(
	val name: String,
	val variance: Variance?,
	val bounds: List<TypeReference>,
	val defaultType: TypeReference?,
	val location: TokenRange
)

@Serializable
data class TypeSpec(
	val isReified: Boolean,
	val parameters: List<TypeParameter>,
	val location: TokenRange
)

enum class Variance {
	IN,
	OUT
}