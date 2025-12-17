package puzzle.core.parser.parser.parameter.context

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.token.kinds.BracketKind
import puzzle.core.token.kinds.ContextualKind
import puzzle.core.token.kinds.OperatorKind
import puzzle.core.token.kinds.SeparatorKind
import puzzle.core.model.span

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseContextSpec(): ContextSpec? {
	if (!cursor.match(ContextualKind.CONTEXT)) return null
	val start = cursor.previous.location
	cursor.expect(BracketKind.Start.LPAREN, "上下文缺少 '('")
	val receivers = buildList {
		do {
			this += parseContextReceiver()
			if (!cursor.check(BracketKind.End.RPAREN)) {
				cursor.expect(SeparatorKind.COMMA, "上下文参数缺少 ','")
			}
		} while (!cursor.match(BracketKind.End.RPAREN))
	}
	val isInherited = cursor.match(OperatorKind.NOT)
	val end = cursor.previous.location
	return ContextSpec(receivers, isInherited, start span end)
}