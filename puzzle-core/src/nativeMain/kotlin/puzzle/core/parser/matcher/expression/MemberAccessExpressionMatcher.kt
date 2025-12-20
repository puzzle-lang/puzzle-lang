package puzzle.core.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.MemberAccessExpression
import puzzle.core.parser.ast.expression.PrefixUnaryExpression
import puzzle.core.parser.parser.statement.parseMemberAccessExpression
import puzzle.core.token.kinds.AccessKind.DOT
import puzzle.core.token.kinds.AccessKind.QUESTION_DOT

object MemberAccessExpressionMatcher : ExpressionMatcher<MemberAccessExpression> {
	
	private val kinds = arrayOf(DOT, QUESTION_DOT)
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		if (left == null) return false
		return kinds.any { cursor.match(it) }
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): MemberAccessExpression {
		if (left!! is PrefixUnaryExpression) {
			syntaxError("访问操作符前不允许使用前缀一元运算符", cursor.previous)
		}
		return parseMemberAccessExpression(left)
	}
}