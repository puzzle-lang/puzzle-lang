package puzzle.core.parser.ast.parameter

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.TokenRange
import puzzle.core.parser.ast.TypeReference

@Serializable
class TypeParameter(
	val name: String,
	val variance: Variance?,
	val bounds: List<TypeReference>,
	val defaultType: TypeReference?,
	val location: TokenRange
)

@Serializable
class TypeSpec(
    val reified: Boolean,
    val parameters: List<TypeParameter>,
    val location: TokenRange
)

enum class Variance {
	IN,
	OUT
}