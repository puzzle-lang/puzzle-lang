package puzzle.core.parser.matcher.declaration

import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.DocComment
import puzzle.core.parser.ast.Modifier
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.TypeSpec

class DeclarationHeader(
	val docComment: DocComment?,
	val annotationCalls: List<AnnotationCall>,
	val typeSpec: TypeSpec?,
	val contextSpec: ContextSpec?,
	val modifiers: List<Modifier>,
)