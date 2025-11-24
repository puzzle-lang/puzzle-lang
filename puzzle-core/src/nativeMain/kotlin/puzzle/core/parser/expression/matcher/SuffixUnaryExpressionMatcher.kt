package puzzle.core.parser.expression.matcher

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.expression.Expression
import puzzle.core.parser.expression.SuffixUnaryExpression
import puzzle.core.parser.expression.parser.SuffixUnaryExpressionParser

object SuffixUnaryExpressionMatcher : ExpressionMatcher<SuffixUnaryExpression> {
	
	private val tokenTypes = listOf(PzlTokenType.DOUBLE_PLUS, PzlTokenType.DOUBLE_MINUS)
	
	override fun match(cursor: PzlTokenCursor, left: Expression?): Boolean {
		return cursor.next.type in tokenTypes
	}
	
	context(_: PzlContext)
	override fun parse(cursor: PzlTokenCursor, left: Expression?): SuffixUnaryExpression {
		return SuffixUnaryExpressionParser.of(cursor).parse()
	}
}