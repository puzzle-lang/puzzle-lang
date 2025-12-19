package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.DocComment
import puzzle.core.parser.ast.Modifier
import puzzle.core.parser.ast.expression.Identifier
import puzzle.core.parser.ast.parameter.Parameter
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.model.SourceLocation

@Serializable
class AnnotationDeclaration(
	val name: Identifier,
	val docComment: DocComment?,
	val modifiers: List<Modifier>,
	val parameters: List<Parameter>,
	val typeSpec: TypeSpec?,
	val annotationCalls: List<AnnotationCall>,
	override val location: SourceLocation,
) : Declaration