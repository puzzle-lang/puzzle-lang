package puzzle.core.parser.matcher.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.LiteralExpression
import puzzle.core.parser.parser.expression.parseLiteralExpression
import puzzle.core.token.kinds.LiteralKind.*
import puzzle.core.token.kinds.LiteralKind.BooleanKind.FALSE
import puzzle.core.token.kinds.LiteralKind.BooleanKind.TRUE

object LiteralExpressionMatcher : ExpressionMatcher<LiteralExpression> {
	
	private val kinds = arrayOf(TRUE, FALSE, NULL)
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return kinds.any { cursor.match(it) } ||
				cursor.match<StringKind>() ||
				cursor.match<CharKind>() ||
				cursor.match<NumberKind>()
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(
		left: Expression?,
	): LiteralExpression = parseLiteralExpression()
}