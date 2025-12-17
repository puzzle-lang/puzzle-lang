package puzzle.core.parser.matcher.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.IfExpression
import puzzle.core.parser.parser.expression.parseIfExpression
import puzzle.core.token.kinds.ControlFlowKind

object IfExpressionMatcher : ExpressionMatcher<IfExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return left == null && cursor.match(ControlFlowKind.IF)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): IfExpression {
		return parseIfExpression()
	}
}