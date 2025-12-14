package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.token.ModifierKind
import puzzle.core.util.ModifierKindListSerializer

@Serializable
class UniqueDeclaration(
	val name: String,
	@Serializable(with = ModifierKindListSerializer::class)
	val modifiers: List<ModifierKind>,
	val contextSpec: ContextSpec?,
	val annotationCalls: List<AnnotationCall>,
	val members: List<Declaration>
) : Declaration