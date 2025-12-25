package puzzle.core.parser.parser.statement

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.IfExpression
import puzzle.core.parser.ast.statement.ExpressionStatement
import puzzle.core.parser.ast.statement.IfStatement
import puzzle.core.parser.ast.statement.Statement
import puzzle.core.parser.parser.expression.parseExpressionChain
import puzzle.core.token.kinds.BracketKind.End.RPAREN
import puzzle.core.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.token.kinds.ControlFlowKind.ELSE

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseIfStatement(): Statement {
	val start = cursor.previous.location
	cursor.expect(LPAREN, "if 语句缺少 '('")
	val condition = parseExpressionChain()
	cursor.expect(RPAREN, "if 语句缺少 ')'")
	val thenStatements = if (cursor.match(LBRACE)) {
		parseStatements()
	} else {
		listOf(parseStatement())
	}
	return if (cursor.match(ELSE)) {
		val elseStatements = if (cursor.match(LBRACE)) {
			parseStatements()
		} else {
			listOf(parseStatement())
		}
		val end = cursor.previous.location
		val expression = IfExpression(
			condition = condition,
			thenStatements = thenStatements,
			elseStatements = elseStatements,
			location = start span end
		)
		ExpressionStatement(parseExpressionChain(expression))
	} else {
		val end = cursor.previous.location
		IfStatement(
			condition = condition,
			thenStatements = thenStatements,
			location = start span end,
		)
	}
}