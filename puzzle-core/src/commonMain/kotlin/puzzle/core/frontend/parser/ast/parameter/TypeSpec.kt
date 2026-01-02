package puzzle.core.frontend.parser.ast.parameter

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.ast.AstNode
import puzzle.core.frontend.parser.ast.expression.Identifier
import puzzle.core.frontend.parser.ast.type.TypeReference

@Serializable
class TypeSpec(
	val reified: Boolean,
	val parameters: List<TypeParameter>,
	override val location: SourceLocation,
) : AstNode

@Serializable
class TypeParameter(
	val name: Identifier,
	val variance: Variance?,
	val bounds: List<TypeReference>,
	val typeExpansion: TypeExpansion?,
	val defaultType: TypeReference?,
	override val location: SourceLocation,
) : AstNode

@Serializable
class Variance(
	val kind: VarianceKind,
	override val location: SourceLocation,
) : AstNode

@Serializable
enum class VarianceKind(
	val value: String,
) {
	
	IN("in"),
	
	OUT("out")
}