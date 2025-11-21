package puzzle.core.parser.parameter.parser

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.parameter.Parameter
import puzzle.core.parser.parameter.parseParameter

context(_: PzlContext)
fun parseFunParameters(cursor: PzlTokenCursor): List<Parameter> {
	cursor.expect(PzlTokenType.LPAREN, "函数缺少 '('")
	if (cursor.match(PzlTokenType.RPAREN)) {
		return emptyList()
	}
	val parameters = mutableListOf<Parameter>()
	while (!cursor.match(PzlTokenType.RPAREN)) {
		parameters += FunParameterParser(cursor).parse()
		if (!cursor.check(PzlTokenType.RPAREN)) {
			cursor.expect(PzlTokenType.COMMA, "参数缺少 ','")
		}
	}
	return parameters
}

private class FunParameterParser(
	private val cursor: PzlTokenCursor
) {
	
	context(_: PzlContext)
	fun parse(): Parameter {
		val modifiers = mutableSetOf<Modifier>()
		modifiers += if (cursor.match(PzlTokenType.VAR)) Modifier.VAR else Modifier.VAL
		return parseParameter(cursor, modifiers)
	}
}