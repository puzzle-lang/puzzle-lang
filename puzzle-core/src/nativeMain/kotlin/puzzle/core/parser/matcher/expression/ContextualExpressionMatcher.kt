package puzzle.core.parser.matcher.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.ContextualExpression
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.parser.expression.parseContextualExpression
import puzzle.core.token.kinds.ContextualKind.SUPER
import puzzle.core.token.kinds.ContextualKind.THIS

object ContextualExpressionMatcher : ExpressionMatcher<ContextualExpression> {
	
	private val kinds = arrayOf(THIS, SUPER)
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return kinds.any { cursor.match(it) }
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): ContextualExpression {
		return parseContextualExpression()
	}
}