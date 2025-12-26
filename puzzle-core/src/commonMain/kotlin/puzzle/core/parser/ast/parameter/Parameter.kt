package puzzle.core.parser.ast.parameter

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.AstNode
import puzzle.core.parser.ast.Modifier
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.Identifier
import puzzle.core.parser.ast.type.TypeReference

@Serializable
class Parameter(
	val name: Identifier,
	val modifiers: List<Modifier>,
	val type: TypeReference,
	val annotationCalls: List<AnnotationCall>,
	val vararg: Vararg?,
	val defaultExpression: Expression?,
	override val location: SourceLocation,
) : AstNode

@Serializable
class Vararg(
	val kind: VarargKind,
	override val location: SourceLocation,
) : AstNode

@Serializable
enum class VarargKind {
	STAR,
	PLUS
}