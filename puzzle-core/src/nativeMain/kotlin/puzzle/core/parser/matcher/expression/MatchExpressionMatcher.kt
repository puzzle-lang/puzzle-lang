package puzzle.core.parser.matcher.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.MatchExpression
import puzzle.core.parser.parser.expression.parseMatchExpression
import puzzle.core.token.kinds.ControlFlowKind

object MatchExpressionMatcher : ExpressionMatcher<MatchExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return left == null && cursor.match(ControlFlowKind.MATCH)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): MatchExpression {
		return parseMatchExpression()
	}
}