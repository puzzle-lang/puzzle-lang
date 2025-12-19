package puzzle.core.parser.parser.expression

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.LoopExpression
import puzzle.core.parser.parser.statement.parseStatements
import puzzle.core.token.kinds.BracketKind.Start.LBRACE

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseLoopExpression(): LoopExpression {
	val start = cursor.previous.location
	cursor.expect(LBRACE, "loop 表达式缺少 '{'")
	val body = parseStatements()
	val end = cursor.previous.location
	return LoopExpression(body, start span end)
}