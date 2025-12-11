package puzzle.core.parser.matcher.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.PrefixUnaryExpression
import puzzle.core.parser.parser.expression.parsePrefixUnaryExpression
import puzzle.core.token.OperatorKind.*

object PrefixUnaryExpressionMatcher : ExpressionMatcher<PrefixUnaryExpression> {
	
	private val tokenTypes = arrayOf(
		PLUS,
		MINUS,
		NOT,
		BIT_NOT,
		DOUBLE_PLUS,
		DOUBLE_MINUS
	)
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		val type = cursor.current.kind
		if (type !in tokenTypes) return false
		return if (left == null || type != PLUS && type != MINUS) {
			cursor.advance()
			true
		} else false
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): PrefixUnaryExpression {
		return parsePrefixUnaryExpression()
	}
}