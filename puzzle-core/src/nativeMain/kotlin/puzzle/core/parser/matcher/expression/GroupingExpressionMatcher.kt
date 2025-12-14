package puzzle.core.parser.matcher.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.parser.expression.parseGroupingExpression
import puzzle.core.token.BracketKind

object GroupingExpressionMatcher : ExpressionMatcher<Expression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match(BracketKind.Start.LPAREN)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): Expression {
		return parseGroupingExpression()
	}
}