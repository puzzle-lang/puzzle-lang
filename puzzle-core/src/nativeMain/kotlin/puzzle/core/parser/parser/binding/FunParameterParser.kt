package puzzle.core.parser.parser.binding

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.*
import puzzle.core.parser.ast.binding.Parameter
import puzzle.core.parser.ast.declaration.NodeKind
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.parser.checkModifiers
import puzzle.core.parser.parser.parseModifiers

context(_: PzlContext)
fun parseFunParameters(cursor: PzlTokenCursor): List<Parameter> {
	cursor.expect(PzlTokenType.LPAREN, "函数缺少 '('")
	if (cursor.match(PzlTokenType.RPAREN)) {
		return emptyList()
	}
	val parameters = mutableListOf<Parameter>()
	while (!cursor.match(PzlTokenType.RPAREN)) {
		parameters += FunParameterParser.of(cursor).parse()
		if (!cursor.check(PzlTokenType.RPAREN)) {
			cursor.expect(PzlTokenType.COMMA, "参数缺少 ','")
		}
	}
	return parameters
}

private class FunParameterParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<FunParameterParser>(::FunParameterParser)
	
	context(_: PzlContext)
	fun parse(): Parameter {
		val modifiers = parseModifiers(cursor)
		checkModifiers(cursor, modifiers, NodeKind.FUN_PARAMETER)
		return ParameterParser.of(cursor).parse(modifiers, isSupportedLambdaType = true)
	}
}