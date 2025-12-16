package puzzle.core.parser.matcher.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.SuffixUnaryExpression
import puzzle.core.parser.parser.expression.parseSuffixUnaryExpression
import puzzle.core.parser.parser.identifier.isIdentifierName
import puzzle.core.token.OperatorKind.DOUBLE_MINUS
import puzzle.core.token.OperatorKind.DOUBLE_PLUS

object SuffixUnaryExpressionMatcher : ExpressionMatcher<SuffixUnaryExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		val nextKind = cursor.next?.kind ?: return false
		if (nextKind != DOUBLE_PLUS && nextKind != DOUBLE_MINUS) return false
		return cursor.current.isIdentifierName()
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): SuffixUnaryExpression {
		return parseSuffixUnaryExpression()
	}
}