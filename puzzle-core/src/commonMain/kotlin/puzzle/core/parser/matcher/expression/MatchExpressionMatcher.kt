package puzzle.core.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.MatchExpression
import puzzle.core.parser.parser.expression.parseMatchExpression
import puzzle.core.token.kinds.ControlFlowKind.MATCH

object MatchExpressionMatcher : ExpressionMatcher, NoPrefixExpressionParser<MatchExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match(MATCH)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("match 前不允许有表达式", cursor.previous)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(): MatchExpression {
		return parseMatchExpression()
	}
}