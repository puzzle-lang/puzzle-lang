package puzzle.core.parser.parser.parameter.context

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.token.kinds.BracketKind.End.RPAREN
import puzzle.core.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.token.kinds.ContextualKind.CONTEXT
import puzzle.core.token.kinds.OperatorKind.NOT
import puzzle.core.token.kinds.SeparatorKind.COMMA

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseContextSpec(): ContextSpec? {
	if (!cursor.match(CONTEXT)) return null
	val start = cursor.previous.location
	cursor.expect(LPAREN, "上下文缺少 '('")
	val receivers = buildList {
		do {
			this += parseContextReceiver()
			if (!cursor.check(RPAREN)) {
				cursor.expect(COMMA, "上下文参数缺少 ','")
			}
		} while (!cursor.match(RPAREN))
	}
	val isInherited = cursor.match(NOT)
	val end = cursor.previous.location
	return ContextSpec(receivers, isInherited, start span end)
}