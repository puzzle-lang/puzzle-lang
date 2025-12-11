package puzzle.core.parser.parser.statement

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.IfExpression
import puzzle.core.parser.ast.statement.ExpressionStatement
import puzzle.core.parser.ast.statement.IfStatement
import puzzle.core.parser.ast.statement.Statement
import puzzle.core.parser.parser.expression.parseExpressionChain
import puzzle.core.token.BracketKind
import puzzle.core.token.ControlFlowKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseIfStatement(): Statement {
	cursor.expect(BracketKind.LPAREN, "if 缺少 '('")
	val condition = parseExpressionChain()
	cursor.expect(BracketKind.RPAREN, "if 缺少 ')'")
	val thenStatements = if (cursor.match(BracketKind.LBRACE)) {
		parseStatements()
	} else {
		listOf(parseStatement())
	}
	return if (cursor.match(ControlFlowKind.ELSE)) {
		val elseStatements = if (cursor.match(BracketKind.LBRACE)) {
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