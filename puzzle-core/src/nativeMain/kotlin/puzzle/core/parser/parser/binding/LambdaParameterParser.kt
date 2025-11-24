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
fun parseLambdaParameters(cursor: PzlTokenCursor): List<Parameter> {
	val parameters = mutableListOf<Parameter>()
	while (!cursor.match(PzlTokenType.RPAREN)) {
		parameters += LambdaParameterParser.of(cursor).parse()
		if (!cursor.check(PzlTokenType.RPAREN)) {
			cursor.expect(PzlTokenType.COMMA, "参数缺少 ','")
		}
	}
	return parameters
}

private class LambdaParameterParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<LambdaParameterParser>(::LambdaParameterParser)
	
	context(_: PzlContext)
	fun parse(): Parameter {
		val modifiers = parseModifiers(cursor)
		checkModifiers(cursor, modifiers, NodeKind.LAMBDA_PARAMETER)
		return ParameterParser.of(cursor).parse(
			isSupportedTypeOnly = true,
			isSupportedLambdaType = true
		)
	}
}