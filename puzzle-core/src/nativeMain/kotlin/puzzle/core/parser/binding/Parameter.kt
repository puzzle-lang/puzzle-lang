package puzzle.core.parser.binding

import kotlinx.serialization.Serializable
import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
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
	isSupportedTypeOnly: Boolean = false,
	isSupportedLambdaType: Boolean = false
): Parameter {
	val name = when {
		isSupportedTypeOnly && cursor.offsetOrNull(offset = 1)?.type != PzlTokenType.COLON -> null
		else -> {
			cursor.expect(PzlTokenType.IDENTIFIER, "参数缺少名称")
			val name = cursor.previous.value
			if (name == "_") {
				syntaxError("参数名称不支持匿名", cursor.previous)
			}
			cursor.expect(PzlTokenType.COLON, "参数缺少 ':'")
			name
		}
	}
	val typeReference = TypeReferenceParser(cursor).parse(isSupportedLambdaType)
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