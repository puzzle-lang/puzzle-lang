package puzzle.core.parser.binding.parser

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.binding.Parameter
import puzzle.core.parser.binding.parseParameter
import puzzle.core.parser.checkModifiers
import puzzle.core.parser.declaration.NodeKind
import puzzle.core.parser.parseModifiers

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
		val modifiers = parseModifiers(cursor)
		checkModifiers(cursor, modifiers, NodeKind.FUN_PARAMETER)
		return parseParameter(cursor, modifiers, isSupportedLambdaType = true)
	}
}