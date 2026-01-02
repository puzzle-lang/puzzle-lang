package puzzle.core.frontend.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.ast.AnnotationCall
import puzzle.core.frontend.parser.ast.DocComment
import puzzle.core.frontend.parser.ast.Modifier
import puzzle.core.frontend.parser.ast.expression.Identifier
import puzzle.core.frontend.parser.ast.parameter.Parameter
import puzzle.core.frontend.parser.ast.parameter.TypeSpec

@Serializable
class AnnotationDeclaration(
	val name: Identifier,
	val docComment: DocComment?,
	val modifiers: List<Modifier>,
	val parameters: List<Parameter>,
	val typeSpec: TypeSpec?,
	val annotationCalls: List<AnnotationCall>,
	override val location: SourceLocation,
) : TopLevelAllowedDeclaration