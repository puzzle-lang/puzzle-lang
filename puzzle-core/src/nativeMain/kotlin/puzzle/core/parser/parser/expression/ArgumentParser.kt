package puzzle.core.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Argument
import puzzle.core.token.kinds.AssignmentKind.ASSIGN
import puzzle.core.token.kinds.BracketKind
import puzzle.core.token.kinds.BracketKind.End.RBRACKET
import puzzle.core.token.kinds.BracketKind.End.RPAREN
import puzzle.core.token.kinds.SeparatorKind.COMMA
import puzzle.core.token.kinds.SeparatorKind.SEMICOLON
import puzzle.core.model.span

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseArguments(endKind: BracketKind.End): List<Argument> {
	if (cursor.match(endKind)) return emptyList()
	val arguments = mutableListOf<Argument>()
	do {
		arguments += parseCallArgument(endKind)
		if (!cursor.check(endKind)) {
			cursor.expect(COMMA, "参数缺少 ','")
		}
	} while (!cursor.match(endKind))
	return arguments
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseCallArgument(endKind: BracketKind.End): Argument {
	val name = if (cursor.offsetOrNull(offset = 1)?.kind == ASSIGN) {
		parseIdentifier(IdentifierTarget.ARGUMENT).also {
			cursor.advance()
		}
	} else null
	val expression = parseExpressionChain()
	val start = name?.location ?: expression.location
	
	if (cursor.previous.kind == SEMICOLON) {
		syntaxError("参数不支持使用 ';' 结束表达式", cursor.previous)
	}
	val end = cursor.previous.location
	val currentType = cursor.current.kind
	if (currentType == COMMA) {
		return Argument(name, expression, start span end)
	}
	return when (endKind) {
		RPAREN if currentType == RBRACKET -> syntaxError(
			message = "函数参数表达式后只允许根 ')' 和 ','",
			token = cursor.current
		)
		
		RBRACKET if currentType == RPAREN -> syntaxError(
			message = "索引访问参数表达式后只允许根 ']' 和 ','",
			token = cursor.current
		)
		
		else -> Argument(name, expression, start span end)
	}
}