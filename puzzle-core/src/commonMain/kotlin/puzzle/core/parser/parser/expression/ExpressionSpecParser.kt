package puzzle.core.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.ExpressionSpec
import puzzle.core.parser.ast.expression.MultiExpressionSpec
import puzzle.core.parser.ast.expression.SingleExpressionSpec
import puzzle.core.token.kinds.BracketKind.End.RBRACKET
import puzzle.core.token.kinds.BracketKind.Start.LBRACKET
import puzzle.core.token.kinds.SeparatorKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseExpressionSpec(): ExpressionSpec? {
	return if (cursor.match(LBRACKET)) {
		val start = cursor.previous.location
		val expressions = buildList {
			while (!cursor.match(RBRACKET)) {
				this += parseExpressionChain()
				if (!cursor.check(RBRACKET)) {
					cursor.expect(SeparatorKind.COMMA, "语句缺少 ','")
				}
			}
		}
		if (expressions.isEmpty()) {
			syntaxError("缺少表达式", cursor.previous)
		}
		if (expressions.size == 1) {
			syntaxError("至少需要2个表达式", cursor.previous)
		}
		val end = cursor.previous.location
		MultiExpressionSpec(expressions, start span end)
	} else {
		val expression = tryParseExpressionChain() ?: return null
		SingleExpressionSpec(expression)
	}
}