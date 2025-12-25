package puzzle.core.parser.parser.expression

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.IfExpression
import puzzle.core.parser.parser.statement.parseStatement
import puzzle.core.parser.parser.statement.parseStatements
import puzzle.core.token.kinds.BracketKind.End.RPAREN
import puzzle.core.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.token.kinds.ControlFlowKind.ELSE

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseIfExpression(): IfExpression {
	val start = cursor.previous.location
	cursor.expect(LPAREN, "if 表达式缺少 '('")
	val condition = parseExpressionChain()
	cursor.expect(RPAREN, "if 表达式缺少 ')'")
	val thenStatements = if (cursor.match(LBRACE)) {
		parseStatements()
	} else {
		listOf(parseStatement())
	}
	cursor.expect(ELSE, "if 表达式缺少 else")
	val elseStatements = if (cursor.match(LBRACE)) {
		parseStatements()
	} else {
		listOf(parseStatement())
	}
	val end = cursor.previous.location
	return IfExpression(
		condition = condition,
		thenStatements = thenStatements,
		elseStatements = elseStatements,
		location = start span end
	)
}