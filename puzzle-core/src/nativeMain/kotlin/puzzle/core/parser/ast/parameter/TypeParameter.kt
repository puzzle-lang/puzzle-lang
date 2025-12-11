package puzzle.core.parser.ast.parameter

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.TokenRange
import puzzle.core.parser.ast.TypeReference
import puzzle.core.token.VarianceKind

@Serializable
class TypeParameter(
	val name: String,
	val variance: VarianceKind?,
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