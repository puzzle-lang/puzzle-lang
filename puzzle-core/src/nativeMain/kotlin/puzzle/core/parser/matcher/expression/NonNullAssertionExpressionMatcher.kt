package puzzle.core.parser.matcher.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.NonNullAssertionExpression
import puzzle.core.parser.parser.expression.parseNonNullAssertionExpression
import puzzle.core.token.kinds.OperatorKind.NOT

object NonNullAssertionExpressionMatcher : ExpressionMatcher<NonNullAssertionExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		if (left == null) return false
		return cursor.match(NOT)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): NonNullAssertionExpression {
		return parseNonNullAssertionExpression(left!!)
	}
}