package puzzle.core.parser.matcher.declaration

import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.DocComment
import puzzle.core.parser.ast.Modifier
import puzzle.core.parser.ast.parameter.DeclarationContextSpec
import puzzle.core.parser.ast.parameter.TypeSpec

class DeclarationHeader(
	val docComment: DocComment?,
	val annotationCalls: List<AnnotationCall>,
	val typeSpec: TypeSpec?,
	val contextSpec: DeclarationContextSpec?,
	val modifiers: List<Modifier>,
)