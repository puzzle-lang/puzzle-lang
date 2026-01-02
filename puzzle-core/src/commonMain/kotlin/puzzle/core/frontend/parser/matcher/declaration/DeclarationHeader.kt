package puzzle.core.frontend.parser.matcher.declaration

import puzzle.core.frontend.parser.ast.AnnotationCall
import puzzle.core.frontend.parser.ast.DocComment
import puzzle.core.frontend.parser.ast.Modifier
import puzzle.core.frontend.parser.ast.parameter.DeclarationContextSpec
import puzzle.core.frontend.parser.ast.parameter.TypeSpec

class DeclarationHeader(
	val docComment: DocComment?,
	val annotationCalls: List<AnnotationCall>,
	val typeSpec: TypeSpec?,
	val contextSpec: DeclarationContextSpec?,
	val modifiers: List<Modifier>,
)