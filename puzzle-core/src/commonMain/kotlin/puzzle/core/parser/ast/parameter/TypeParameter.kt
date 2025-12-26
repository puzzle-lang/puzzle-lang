package puzzle.core.parser.ast.parameter

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.AstNode
import puzzle.core.parser.ast.type.TypeReference
import puzzle.core.parser.ast.expression.Identifier
import puzzle.core.token.kinds.VarianceKind
import puzzle.core.util.VarianceKindSerializer

@Serializable
class TypeParameter(
	val name: Identifier,
	val variance: Variance?,
	val bounds: List<TypeReference>,
	val defaultType: TypeReference?,
	override val location: SourceLocation,
) : AstNode

@Serializable
class TypeSpec(
	val reified: Boolean,
	val parameters: List<TypeParameter>,
	override val location: SourceLocation,
) : AstNode

@Serializable
class Variance(
	@Serializable(with = VarianceKindSerializer::class)
	val kind: VarianceKind,
	override val location: SourceLocation,
) : AstNode