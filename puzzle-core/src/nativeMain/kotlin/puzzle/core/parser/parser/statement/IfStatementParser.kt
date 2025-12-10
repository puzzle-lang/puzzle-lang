package puzzle.core.parser.parser.statement

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.IfExpression
import puzzle.core.parser.ast.statement.ExpressionStatement
import puzzle.core.parser.ast.statement.IfStatement
import puzzle.core.parser.ast.statement.Statement
import puzzle.core.parser.parser.expression.parseExpressionChain

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseIfStatement(): Statement {
	cursor.expect(PzlTokenType.LPAREN, "if 缺少 '('")
	val condition = parseExpressionChain()
	cursor.expect(PzlTokenType.RPAREN, "if 缺少 ')'")
	val thenStatements = if (cursor.match(PzlTokenType.LBRACKET)) {
		parseStatements()
	} else {
		listOf(parseStatement())
	}
	return if (cursor.match(PzlTokenType.ELSE)) {
		val elseStatements = if (cursor.match(PzlTokenType.LBRACKET)) {
			parseStatements()
		} else {
			listOf(parseStatement())
		}
		val expression = IfExpression(
			condition = condition,
			thenStatements = thenStatements,
			elseStatements = elseStatements
		)
		ExpressionStatement(expression)
	} else {
		IfStatement(
			condition = condition,
			thenStatements = thenStatements
		)
	}
}