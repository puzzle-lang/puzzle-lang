package puzzle.core.parser.parser.binding

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.Parameter
import puzzle.core.parser.ast.declaration.NodeKind
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.parser.checkModifiers
import puzzle.core.parser.parser.parseModifiers

context(_: PzlContext)
fun parseEnumParameters(cursor: PzlTokenCursor): List<Parameter> {
	if (!cursor.match(PzlTokenType.LPAREN)) {
		return emptyList()
	}
	val parameters = mutableListOf<Parameter>()
	while (!cursor.match(PzlTokenType.RPAREN)) {
		parameters += EnumParameterParser.of(cursor).parse()
		if (!cursor.check(PzlTokenType.RPAREN)) {
			cursor.expect(PzlTokenType.COMMA, "参数缺少 ','")
		}
	}
	return parameters
}

private class EnumParameterParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<EnumParameterParser>(::EnumParameterParser)
	
	context(_: PzlContext)
	fun parse(): Parameter {
		val modifiers = parseModifiers(cursor)
		checkModifiers(cursor, modifiers, NodeKind.ENUM_PARAMETER)
		return ParameterParser.of(cursor).parse(modifiers, isSupportedLambdaType = true)
	}
}