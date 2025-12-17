package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.Modifier
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.IdentifierExpression
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.token.SourceLocation

@Serializable
class PropertyDeclaration(
	val name: IdentifierExpression,
	val type: TypeReference?,
	val modifiers: List<Modifier>,
	val extension: TypeReference?,
	val typeSpec: TypeSpec?,
	val contextSpec: ContextSpec?,
	val annotationCalls: List<AnnotationCall>,
	val initializer: Expression?,
	override val location: SourceLocation,
) : Declaration