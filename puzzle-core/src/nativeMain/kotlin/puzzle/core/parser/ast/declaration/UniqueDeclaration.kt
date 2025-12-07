package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.symbol.Modifier

@Serializable
class UniqueDeclaration(
	val name: String,
	val modifiers: List<Modifier>,
	val contextSpec: ContextSpec?,
	val annotationCalls: List<AnnotationCall>,
	val members: List<Declaration>
) : Declaration