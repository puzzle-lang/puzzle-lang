package puzzle.core.parser.matcher.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.MemberReferenceExpression
import puzzle.core.parser.parser.statement.parseMemberReferenceExpression
import puzzle.core.token.kinds.AccessKind.*

object MemberReferenceExpressionMatcher : ExpressionMatcher<MemberReferenceExpression> {
	
	private val kinds = arrayOf(DOT, QUESTION_DOT)
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match(DOUBLE_COLON)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): MemberReferenceExpression {
		return parseMemberReferenceExpression(left)
	}
}