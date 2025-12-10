package puzzle.core.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Argument
import puzzle.core.parser.ast.expression.InvokeType
import puzzle.core.parser.ast.expression.InvokeType.CALL
import puzzle.core.parser.ast.expression.InvokeType.INDEX_ACCESS
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseIdentifierName

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseArguments(type: InvokeType): List<Argument> {
	if (cursor.match(type.endTokenType)) return emptyList()
	val arguments = mutableListOf<Argument>()
	do {
		arguments += parseCallArgument(type)
		if (!cursor.check(type.endTokenType)) {
			cursor.expect(PzlTokenType.COMMA, "参数缺少 ','")
		}
	} while (!cursor.match(type.endTokenType))
	return arguments
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseCallArgument(type: InvokeType): Argument {
	val name = if (cursor.offsetOrNull(offset = 1)?.type == PzlTokenType.ASSIGN) {
		parseIdentifierName(IdentifierNameTarget.ARGUMENT).also {
			cursor.advance()
		}
	} else null
	val expression = parseExpressionChain()
	if (cursor.previous.type == PzlTokenType.SEMICOLON) {
		syntaxError("语法错误", cursor.previous)
	}
	val currentType = cursor.current.type
	if (currentType == PzlTokenType.COMMA) {
		return Argument(name, expression)
	}
	when (type) {
		CALL if (currentType == INDEX_ACCESS.endTokenType) -> syntaxError(
			"函数参数表达式后只允许根 ')' 和 ','",
			cursor.current
		)
		
		INDEX_ACCESS if (currentType == CALL.endTokenType) -> syntaxError(
			"索引访问参数表达式后只允许根 ']' 和 ','",
			cursor.current
		)
		
		else -> return Argument(name, expression)
	}
}