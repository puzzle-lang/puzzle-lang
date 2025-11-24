package puzzle.core.parser.matcher.expression

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.parser.expression.GroupingExpressionParser

object GroupingExpressionMatcher : ExpressionMatcher<Expression> {
	
	override fun match(cursor: PzlTokenCursor, left: Expression?): Boolean {
		return cursor.match(PzlTokenType.LPAREN)
	}
	
	context(_: PzlContext)
	override fun parse(cursor: PzlTokenCursor, left: Expression?): Expression {
		return GroupingExpressionParser.of(cursor).parse()
	}
}