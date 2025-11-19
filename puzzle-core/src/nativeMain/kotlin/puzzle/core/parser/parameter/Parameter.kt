package puzzle.core.parser.parameter

import kotlinx.serialization.Serializable
import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.expression.Expression
import puzzle.core.parser.expression.matcher.parseCompleteExpression
import puzzle.core.parser.node.TypeReference
import puzzle.core.parser.node.parser.TypeReferenceParser

@Serializable
data class Parameter(
	val name: String?,
	val modifiers: Set<Modifier>,
	val typeReference: TypeReference,
	val defaultExpression: Expression? = null
)

context(_: PzlContext)
fun parseParameter(
	ctx: PzlParserContext,
	modifiers: Set<Modifier> = emptySet(),
	isSupportedAnonymous: Boolean = false,
	isSupportedLambdaType: Boolean = true
): Parameter {
	val name = if (isSupportedAnonymous && ctx.peek(offset = 1)?.type != PzlTokenType.COLON) null else {
		ctx.expect(PzlTokenType.IDENTIFIER, "参数缺少名称")
		ctx.previous.value.also {
			ctx.expect(PzlTokenType.COLON, "参数缺少 ':'")
		}
	}
	val typeReference = TypeReferenceParser(ctx).parse(
		isSupportedLambdaType = isSupportedLambdaType
	)
	val defaultExpression = if (ctx.match(PzlTokenType.ASSIGN)) {
		parseCompleteExpression(ctx)
	} else null
	return Parameter(
		name = name,
		modifiers = modifiers,
		typeReference = typeReference,
		defaultExpression = defaultExpression
	)
}