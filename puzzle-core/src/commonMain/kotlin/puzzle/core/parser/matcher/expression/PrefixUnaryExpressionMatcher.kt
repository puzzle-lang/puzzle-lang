package puzzle.core.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.PrefixUnaryExpression
import puzzle.core.parser.parser.expression.parsePrefixUnaryExpression
import puzzle.core.token.kinds.OperatorKind.*

object PrefixUnaryExpressionMatcher : ExpressionMatcher, NoPrefixExpressionParser<PrefixUnaryExpression> {
	
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
		return cursor.match {
			it.kind in kinds && (left == null || (it.kind != PLUS && it.kind != MINUS))
		}
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("'${cursor.previous.value}' 前不允许有表达式", cursor.previous)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(): PrefixUnaryExpression {
		return parsePrefixUnaryExpression()
	}
}