package puzzle.core.parser.parser.expression

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.IfExpression
import puzzle.core.parser.parser.statement.parseStatement
import puzzle.core.parser.parser.statement.parseStatements

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseIfExpression(): IfExpression {
	cursor.expect(PzlTokenType.LPAREN, "if 表达式缺少 '('")
	val condition = parseExpressionChain()
	cursor.expect(PzlTokenType.RPAREN, "if 表达式缺少 ')'")
	val thenStatements = if (cursor.match(PzlTokenType.LBRACKET)) {
		parseStatements()
	} else {
		listOf(parseStatement())
	}
	cursor.expect(PzlTokenType.ELSE, "if 表达式后缺少 else")
	val elseStatements = if (cursor.match(PzlTokenType.LBRACKET)) {
		parseStatements()
	} else {
		listOf(parseStatement())
	}
	return IfExpression(
		condition = condition,
		thenStatements = thenStatements,
		elseStatements = elseStatements
	)
}