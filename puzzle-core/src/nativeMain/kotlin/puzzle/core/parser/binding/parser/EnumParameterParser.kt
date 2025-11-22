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
fun parseEnumParameters(cursor: PzlTokenCursor): List<Parameter> {
	if (!cursor.match(PzlTokenType.LPAREN)) {
		return emptyList()
	}
	val parameters = mutableListOf<Parameter>()
	while (!cursor.match(PzlTokenType.RPAREN)) {
		parameters += EnumParameterParser(cursor).parse()
		if (!cursor.check(PzlTokenType.RPAREN)) {
			cursor.expect(PzlTokenType.COMMA, "参数缺少 ','")
		}
	}
	return parameters
}

private class EnumParameterParser(
	private val cursor: PzlTokenCursor
) {
	
	context(_: PzlContext)
	fun parse(): Parameter {
		val modifiers = parseModifiers(cursor)
		checkModifiers(cursor, modifiers, NodeKind.ENUM_PARAMETER)
		return parseParameter(cursor, modifiers)
	}
}