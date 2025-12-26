package puzzle.core.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Argument
import puzzle.core.token.kinds.AssignmentKind.ASSIGN
import puzzle.core.token.kinds.BracketKind.End.RBRACKET
import puzzle.core.token.kinds.BracketKind.End.RPAREN
import puzzle.core.token.kinds.SeparatorKind.COMMA
import puzzle.core.token.kinds.SeparatorKind.SEMICOLON

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseArguments(isParen: Boolean = true): List<Argument> {
	val endKind = if (isParen) RPAREN else RBRACKET
	if (cursor.match(endKind)) return emptyList()
	return buildList {
		do {
			this += parseCallArgument(isParen)
			if (!cursor.check(endKind)) {
				cursor.expect(COMMA, "参数缺少 ','")
			}
		} while (!cursor.match(endKind))
	}
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseCallArgument(isParen: Boolean): Argument {
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
	val currentKind = cursor.current.kind
	if (currentKind == COMMA) {
		return Argument(name, expression, start span end)
	}
	when {
		isParen && currentKind == RBRACKET -> syntaxError(
			message = "参数表达式后只允许根 ')' 或 ','",
			token = cursor.current
		)
		
		!isParen && currentKind == RPAREN -> syntaxError(
			message = "索引访问参数表达式后只允许根 ']' 或 ','",
			token = cursor.current
		)
	}
	return Argument(name, expression, start span end)
}