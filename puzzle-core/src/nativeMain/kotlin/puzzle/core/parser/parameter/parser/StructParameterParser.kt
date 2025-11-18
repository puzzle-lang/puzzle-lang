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
fun parseStructParameters(ctx: PzlParserContext, structAccess: Modifier): List<Parameter> {
	ctx.expect(PzlTokenType.LPAREN, "结构体缺少 '('")
	val parameters = mutableListOf<Parameter>()
	while (!ctx.match(PzlTokenType.RPAREN)) {
		parameters += StructParameterParser(ctx).parse(structAccess)
		if (!ctx.check(PzlTokenType.RPAREN)) {
			ctx.expect(PzlTokenType.COMMA, "参数缺少 ','")
		}
	}
	return parameters
}

private class StructParameterParser(
	private val ctx: PzlParserContext
) {
	
	context(_: PzlContext)
	fun parse(structAccess: Modifier): Parameter {
		val modifiers = mutableSetOf<Modifier>()
		modifiers += getDefaultMemberAccessModifier(structAccess)
		if (ctx.match(PzlTokenType.IGNORE)) {
			modifiers += Modifier.IGNORE
		}
		modifiers += when {
			ctx.match(PzlTokenType.VAR) -> Modifier.VAR
			ctx.match(PzlTokenType.VAL) -> Modifier.VAL
			else -> syntaxError("结构体参数必须添加 'var' 或 'val' 修饰符", ctx.current)
		}
		return parseParameter(ctx, modifiers, isSupportedLambdaType = false)
	}
}