package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.Parameter
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.token.ModifierKind
import puzzle.core.util.ModifierKindListSerializer

@Serializable
class EnumDeclaration(
	val name: String,
	@Serializable(with = ModifierKindListSerializer::class)
	val modifiers: List<ModifierKind>,
	val parameters: List<Parameter>,
	val entries: List<EnumEntry>,
	val typeSpec: TypeSpec?,
	val contextSpec: ContextSpec?,
	val annotationCalls: List<AnnotationCall>,
	val members: List<Declaration>,
) : Declaration

@Serializable
class EnumEntry(
	val name: String,
	val members: List<Declaration>,
)