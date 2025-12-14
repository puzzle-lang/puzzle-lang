package puzzle.core.parser.parser.parameter.context

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.token.BracketKind
import puzzle.core.token.ContextualKind
import puzzle.core.token.OperatorKind
import puzzle.core.token.SeparatorKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseContextSpec(): ContextSpec? {
	if (!cursor.match(ContextualKind.CONTEXT)) return null
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
	return ContextSpec(receivers, isInherited)
}