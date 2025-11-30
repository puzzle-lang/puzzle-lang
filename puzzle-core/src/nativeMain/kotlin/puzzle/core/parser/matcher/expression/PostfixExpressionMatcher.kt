package puzzle.core.parser.matcher.expression

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType.*
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.parser.expression.PostfixExpressionParser
import puzzle.core.parser.parser.identifier.IdentifierNameParser

object PostfixExpressionMatcher : ExpressionMatcher<Expression> {
	
	private val tokenTypes = arrayOf(
		NUMBER, STRING, CHAR, TRUE, FALSE,
		THIS, SUPER, NULL,
		DOT, QUESTION_DOT, DOUBLE_COLON
	)
	
	override fun match(cursor: PzlTokenCursor, left: Expression?): Boolean {
		return tokenTypes.any { cursor.match(it) } || IdentifierNameParser.of(cursor).match()
	}
	
	context(_: PzlContext)
	override fun parse(cursor: PzlTokenCursor, left: Expression?): Expression {
		return PostfixExpressionParser.of(cursor).parse(left)
	}
}