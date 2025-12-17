package puzzle.core.parser.ast.parameter

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.AstNode
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.ast.expression.IdentifierExpression
import puzzle.core.token.SourceLocation
import puzzle.core.token.kinds.VarianceKind

@Serializable
class TypeParameter(
	val name: IdentifierExpression,
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
	val kind: VarianceKind,
	override val location: SourceLocation,
) : AstNode