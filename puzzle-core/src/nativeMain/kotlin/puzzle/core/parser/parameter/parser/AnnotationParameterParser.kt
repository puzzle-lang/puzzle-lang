package puzzle.core.parser.parameter.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.getDefaultMemberAccessModifier
import puzzle.core.parser.parameter.Parameter
import puzzle.core.parser.parameter.parseParameter

context(_: PzlContext)
fun parseAnnotationParameters(
	ctx: PzlParserContext,
	annotationAccess: Modifier
): List<Parameter> {
	if (!ctx.match(PzlTokenType.LPAREN)) {
		return emptyList()
	}
	val parameters = mutableListOf<Parameter>()
	while (!ctx.match(PzlTokenType.RPAREN)) {
		parameters += AnnotationParameterParser(ctx).parse(annotationAccess)
		if (!ctx.check(PzlTokenType.RPAREN)) {
			ctx.expect(PzlTokenType.COMMA, "参数缺少 ','")
		}
	}
	return parameters
}

class AnnotationParameterParser(
	private val ctx: PzlParserContext
) {
	
	context(_: PzlContext)
	fun parse(annotationAccess: Modifier): Parameter {
		val modifiers = mutableSetOf<Modifier>()
		modifiers += getDefaultMemberAccessModifier(annotationAccess)
		modifiers += when {
			ctx.match(PzlTokenType.VAR) -> syntaxError("注解参数不支持 'var' 修饰符", ctx.current)
			ctx.match(PzlTokenType.VAL) -> Modifier.VAL
			else -> syntaxError("注解参数必须添加 'val' 修饰符", ctx.current)
		}
		return parseParameter(ctx, modifiers, isSupportedLambdaType = false)
	}
}