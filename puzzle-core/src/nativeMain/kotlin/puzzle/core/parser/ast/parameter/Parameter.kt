package puzzle.core.parser.ast.parameter

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.token.ModifierKind
import puzzle.core.util.ModifierKindListSerializer

@Serializable
class Parameter(
	val name: String?,
	@Serializable(with = ModifierKindListSerializer::class)
	val modifiers: List<ModifierKind>,
	val type: TypeReference,
	val annotationCalls: List<AnnotationCall>,
	val defaultExpression: Expression? = null
)