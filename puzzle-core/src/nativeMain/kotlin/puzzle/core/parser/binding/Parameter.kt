package puzzle.core.parser.binding

import kotlinx.serialization.Serializable
import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.expression.Expression
import puzzle.core.parser.expression.matcher.parseCompleteExpression
import puzzle.core.parser.node.TypeReference
import puzzle.core.parser.node.parser.TypeReferenceParser

@Serializable
data class Parameter(
	val name: String?,
	val modifiers: List<Modifier>,
	val typeReference: TypeReference,
	val defaultExpression: Expression? = null
)

context(_: PzlContext)
fun parseParameter(
	cursor: PzlTokenCursor,
	modifiers: List<Modifier> = emptyList(),
	isSupportedAnonymous: Boolean = false,
	isSupportedLambdaType: Boolean = true
): Parameter {
	val name = if (isSupportedAnonymous && cursor.offsetOrNull(offset = 1)?.type != PzlTokenType.COLON) null else {
		cursor.expect(PzlTokenType.IDENTIFIER, "参数缺少名称")
		cursor.previous.value.also {
			cursor.expect(PzlTokenType.COLON, "参数缺少 ':'")
		}
	}
	val typeReference = TypeReferenceParser(cursor).parse(
		isSupportedLambdaType = isSupportedLambdaType
	)
	val defaultExpression = if (cursor.match(PzlTokenType.ASSIGN)) {
		parseCompleteExpression(cursor)
	} else null
	return Parameter(
		name = name,
		modifiers = modifiers,
		typeReference = typeReference,
		defaultExpression = defaultExpression
	)
}