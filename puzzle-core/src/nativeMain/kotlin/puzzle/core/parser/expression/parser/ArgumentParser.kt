package puzzle.core.parser.expression.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlParser
import puzzle.core.parser.PzlParserProvider
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.expression.Argument
import puzzle.core.parser.expression.InvokeType
import puzzle.core.parser.expression.InvokeType.CALL
import puzzle.core.parser.expression.InvokeType.INDEX_ACCESS
import puzzle.core.parser.expression.matcher.parseCompleteExpression

context(_: PzlContext)
fun parseArguments(cursor: PzlTokenCursor, type: InvokeType): List<Argument> {
	if (cursor.match(type.endTokenType)) return emptyList()
	val arguments = mutableListOf<Argument>()
	do {
		arguments += CallArgumentParser.of(cursor).parse(type)
		if (!cursor.check(type.endTokenType)) {
			cursor.expect(PzlTokenType.COMMA, "参数缺少 ','")
		}
	} while (!cursor.match(type.endTokenType))
	return arguments
}

private class CallArgumentParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<CallArgumentParser>(::CallArgumentParser)
	
	context(_: PzlContext)
	fun parse(type: InvokeType): Argument {
		val name = if (cursor.offsetOrNull(offset = 1)?.type == PzlTokenType.ASSIGN) {
			cursor.expect(PzlTokenType.IDENTIFIER, "型参名称必须为标识符")
			val name = cursor.previous.value
			cursor.expect(PzlTokenType.ASSIGN, "型参名称后必须为 '='")
			name
		} else null
		val expression = parseCompleteExpression(cursor)
		if (cursor.previous.type == PzlTokenType.SEMICOLON) {
			syntaxError("语法错误", cursor.previous)
		}
		val currentType = cursor.current.type
		if (currentType == PzlTokenType.COMMA) {
			return Argument(name, expression)
		}
		when (type) {
			CALL if (currentType == INDEX_ACCESS.endTokenType) -> syntaxError("函数参数表达式后只允许根 ')' 和 ','", cursor.current)
			INDEX_ACCESS if (currentType == CALL.endTokenType) -> syntaxError("索引访问参数表达式后只允许根 ']' 和 ','", cursor.current)
			else -> return Argument(name, expression)
		}
	}
}