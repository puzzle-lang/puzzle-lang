package puzzle.core.parser.parser.statement

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.statement.WhileKind
import puzzle.core.parser.ast.statement.WhileStatement
import puzzle.core.parser.parser.expression.parseExpressionChain
import puzzle.core.token.kinds.BracketKind.End.RPAREN
import puzzle.core.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.token.kinds.ControlFlowKind.DO
import puzzle.core.token.kinds.ControlFlowKind.WHILE

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseWhileStatement(): WhileStatement {
	val token = cursor.previous
	val start = token.location
	val kind = if (token.kind == DO) WhileKind.DO_WHILE else WhileKind.WHILE
	var condition: Expression? = null
	if (kind == WhileKind.WHILE) {
		cursor.expect(LPAREN, "while 语句缺少 '('")
		condition = parseExpressionChain()
		cursor.expect(RPAREN, "while 语句缺少 ')'")
	}
	val type = if (kind == WhileKind.WHILE) "while" else "do"
	cursor.expect(LBRACE, "$type 语句缺少 '{'")
	val body = parseStatements()
	if (kind == WhileKind.DO_WHILE) {
		cursor.expect(WHILE, "do 语句缺少 while")
		cursor.expect(LPAREN, "while 语句缺少 '('")
		condition = parseExpressionChain()
		cursor.expect(RPAREN, "while 语句缺少 ')'")
	}
	val end = cursor.previous.location
	return WhileStatement(condition!!, kind, body, start span end)
}