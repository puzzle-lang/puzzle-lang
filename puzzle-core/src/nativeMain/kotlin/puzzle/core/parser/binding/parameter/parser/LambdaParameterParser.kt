package puzzle.core.parser.binding.parameter.parser

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.*
import puzzle.core.parser.binding.parameter.Parameter
import puzzle.core.parser.declaration.NodeKind

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