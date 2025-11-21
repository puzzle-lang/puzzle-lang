package puzzle.core.parser.expression.matcher

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.expression.Expression
import puzzle.core.parser.expression.parser.GroupingExpressionParser

object GroupingExpressionMatcher : ExpressionMatcher<Expression> {
	
	override fun match(cursor: PzlTokenCursor, left: Expression?): Boolean {
		return cursor.match(PzlTokenType.LPAREN)
	}
	
	context(_: PzlContext)
	override fun parse(cursor: PzlTokenCursor, left: Expression?): Expression {
		return GroupingExpressionParser(cursor).parse()
	}
}