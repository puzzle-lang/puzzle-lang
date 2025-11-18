package puzzle.core.parser.parameter.parser

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.parameter.Parameter
import puzzle.core.parser.parameter.parseParameter

context(_: PzlContext)
fun parseLambdaParameters(ctx: PzlParserContext): List<Parameter> {
	val parameters = mutableListOf<Parameter>()
	while (!ctx.match(PzlTokenType.RPAREN)) {
		parameters += LambdaParameterParser(ctx).parse()
		if (!ctx.check(PzlTokenType.RPAREN)) {
			ctx.expect(PzlTokenType.COMMA, "参数缺少 ','")
		}
	}
	return parameters
}

private class LambdaParameterParser(
	private val ctx: PzlParserContext
) {
	
	context(_: PzlContext)
	fun parse(): Parameter {
		return parseParameter(ctx, isSupportedAnonymous = true)
	}
}