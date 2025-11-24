package puzzle.core.parser.matcher.expression

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.PrefixUnaryExpression
import puzzle.core.parser.parser.expression.PrefixUnaryExpressionParser

object PrefixUnaryExpressionMatcher : ExpressionMatcher<PrefixUnaryExpression> {
	
	private val tokenTypes = listOf(
		PzlTokenType.PLUS,
		PzlTokenType.MINUS,
		PzlTokenType.BANG,
		PzlTokenType.BIT_NOT,
		PzlTokenType.DOUBLE_PLUS,
		PzlTokenType.DOUBLE_MINUS
	)
	
	override fun match(cursor: PzlTokenCursor, left: Expression?): Boolean {
		val type = cursor.current.type
		if (type !in tokenTypes) return false
		return if (left == null || type != PzlTokenType.PLUS && type != PzlTokenType.MINUS) {
			cursor.advance()
			true
		} else false
	}
	
	context(_: PzlContext)
	override fun parse(cursor: PzlTokenCursor, left: Expression?): PrefixUnaryExpression {
		return PrefixUnaryExpressionParser.of(cursor).parse()
	}
}