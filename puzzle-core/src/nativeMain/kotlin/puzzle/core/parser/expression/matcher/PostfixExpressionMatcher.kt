package puzzle.core.parser.expression.matcher

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType.*
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.expression.Expression
import puzzle.core.parser.expression.parser.PostfixExpressionParser

object PostfixExpressionMatcher : ExpressionMatcher<Expression> {
	
	private val tokenTypes = listOf(
		NUMBER, STRING, CHAR, TRUE, FALSE, IDENTIFIER,
		THIS, SUPER, NULL,
		DOT, QUESTION_DOT, DOUBLE_COLON
	)
	
	override fun match(cursor: PzlTokenCursor, left: Expression?): Boolean {
		return tokenTypes.any { cursor.match(it) }
	}
	
	context(_: PzlContext)
	override fun parse(cursor: PzlTokenCursor, left: Expression?): Expression {
		return PostfixExpressionParser(cursor).parse(left)
	}
}