package puzzle.core.parser.matcher.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.SuffixUnaryExpression
import puzzle.core.parser.parser.expression.parseSuffixUnaryExpression
import puzzle.core.token.OperatorKind

object SuffixUnaryExpressionMatcher : ExpressionMatcher<SuffixUnaryExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		val type = cursor.next?.kind
		return type == OperatorKind.DOUBLE_PLUS || type == OperatorKind.DOUBLE_MINUS
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): SuffixUnaryExpression {
		return parseSuffixUnaryExpression()
	}
}