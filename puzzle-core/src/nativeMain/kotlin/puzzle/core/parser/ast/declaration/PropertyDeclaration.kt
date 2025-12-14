package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.token.ModifierKind
import puzzle.core.util.ModifierKindListSerializer

@Serializable
class PropertyDeclaration(
	val name: String,
	val type: TypeReference?,
	@Serializable(with = ModifierKindListSerializer::class)
	val modifiers: List<ModifierKind>,
	val extension: TypeReference?,
	val typeSpec: TypeSpec?,
	val contextSpec: ContextSpec?,
	val annotationCalls: List<AnnotationCall>,
	val initializer: Expression?
) : Declaration