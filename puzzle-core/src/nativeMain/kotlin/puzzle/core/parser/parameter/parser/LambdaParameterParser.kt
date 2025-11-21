package puzzle.core.parser.parameter.parser

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.parameter.Parameter
import puzzle.core.parser.parameter.parseParameter

context(_: PzlContext)
fun parseLambdaParameters(cursor: PzlTokenCursor): List<Parameter> {
	val parameters = mutableListOf<Parameter>()
	while (!cursor.match(PzlTokenType.RPAREN)) {
		parameters += LambdaParameterParser(cursor).parse()
		if (!cursor.check(PzlTokenType.RPAREN)) {
			cursor.expect(PzlTokenType.COMMA, "参数缺少 ','")
		}
	}
	return parameters
}

private class LambdaParameterParser(
	private val cursor: PzlTokenCursor
) {
	
	context(_: PzlContext)
	fun parse(): Parameter {
		return parseParameter(cursor, isSupportedAnonymous = true)
	}
}