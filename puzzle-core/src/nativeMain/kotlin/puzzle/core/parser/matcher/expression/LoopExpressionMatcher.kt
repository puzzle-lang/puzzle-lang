package puzzle.core.parser.matcher.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.LoopExpression
import puzzle.core.parser.parser.expression.parseLoopExpression
import puzzle.core.token.kinds.ControlFlowKind.LOOP

object LoopExpressionMatcher : ExpressionMatcher<LoopExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match(LOOP)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): LoopExpression {
		return parseLoopExpression()
	}
}