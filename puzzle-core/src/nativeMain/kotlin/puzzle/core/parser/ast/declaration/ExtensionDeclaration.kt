package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.token.ModifierKind
import puzzle.core.util.ModifierKindListSerializer

@Serializable
class ExtensionDeclaration(
	val extendedType: TypeReference,
	@Serializable(with = ModifierKindListSerializer::class)
	val modifiers: List<ModifierKind>,
	val superTraits: List<SuperTrait>,
	val typeSpec: TypeSpec?,
	val contextSpec: ContextSpec?,
	val annotationCalls: List<AnnotationCall>,
	val members: List<Declaration>
) : Declaration