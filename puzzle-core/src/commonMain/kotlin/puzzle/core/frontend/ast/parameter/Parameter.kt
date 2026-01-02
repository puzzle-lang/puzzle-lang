package puzzle.core.frontend.ast.parameter

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.ast.AnnotationCall
import puzzle.core.frontend.ast.AstNode
import puzzle.core.frontend.ast.Modifier
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.ast.type.TypeReference

@Serializable
class Parameter(
	val name: Identifier,
	val modifiers: List<Modifier>,
	val type: TypeReference,
	val annotationCalls: List<AnnotationCall>,
	val quantifier: Quantifier?,
	val defaultExpression: Expression?,
	override val location: SourceLocation,
) : AstNode