package puzzle.core.frontend.parser.ast.parameter

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.ast.AnnotationCall
import puzzle.core.frontend.parser.ast.AstNode
import puzzle.core.frontend.parser.ast.Modifier
import puzzle.core.frontend.parser.ast.expression.Expression
import puzzle.core.frontend.parser.ast.expression.Identifier
import puzzle.core.frontend.parser.ast.type.TypeReference

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