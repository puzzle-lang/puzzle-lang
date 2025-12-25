package puzzle.core.parser.ast.parameter

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.AstNode
import puzzle.core.parser.ast.Modifier
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.Identifier

@Serializable
class Parameter(
	val name: Identifier,
	val modifiers: List<Modifier>,
	val type: TypeReference,
	val annotationCalls: List<AnnotationCall>,
	val defaultExpression: Expression?,
	override val location: SourceLocation,
) : AstNode