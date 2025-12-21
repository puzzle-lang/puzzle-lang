package puzzle.core.parser.matcher.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.PrefixUnaryExpression
import puzzle.core.parser.parser.expression.parsePrefixUnaryExpression
import puzzle.core.token.kinds.OperatorKind.*

object PrefixUnaryExpressionMatcher : ExpressionMatcher<PrefixUnaryExpression> {
	
	private val kinds = setOf(
		PLUS,
		MINUS,
		NOT,
		BIT_NOT,
		DOUBLE_PLUS,
		DOUBLE_MINUS
	)
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		val kind = cursor.current.kind
		if (kind !in kinds) return false
		return if (left == null || kind != PLUS && kind != MINUS) {
			cursor.advance()
			true
		} else false
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): PrefixUnaryExpression {
		return parsePrefixUnaryExpression()
	}
}