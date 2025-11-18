package puzzle.core.parser.parameter.parser

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.parameter.Parameter
import puzzle.core.parser.parameter.parseParameter

context(_: PzlContext)
fun parseFunParameters(ctx: PzlParserContext): List<Parameter> {
	ctx.expect(PzlTokenType.LPAREN, "函数缺少 '('")
	if (ctx.match(PzlTokenType.RPAREN)) {
		return emptyList()
	}
	val parameters = mutableListOf<Parameter>()
	while (!ctx.match(PzlTokenType.RPAREN)) {
		parameters += FunParameterParser(ctx).parse()
		if (!ctx.check(PzlTokenType.RPAREN)) {
			ctx.expect(PzlTokenType.COMMA, "参数缺少 ','")
		}
	}
	return parameters
}

private class FunParameterParser(
	private val ctx: PzlParserContext
) {
	
	context(_: PzlContext)
	fun parse(): Parameter {
		val modifiers = mutableSetOf<Modifier>()
		modifiers += if (ctx.match(PzlTokenType.VAR)) Modifier.VAR else Modifier.VAL
		return parseParameter(ctx, modifiers)
	}
}