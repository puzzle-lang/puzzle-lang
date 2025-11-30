package puzzle.core.parser.matcher.expression

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.SuffixUnaryExpression
import puzzle.core.parser.parser.expression.SuffixUnaryExpressionParser

object SuffixUnaryExpressionMatcher : ExpressionMatcher<SuffixUnaryExpression> {
	
	override fun match(cursor: PzlTokenCursor, left: Expression?): Boolean {
		val type = cursor.next.type
		return type == PzlTokenType.DOUBLE_PLUS || type == PzlTokenType.DOUBLE_MINUS
	}
	
	context(_: PzlContext)
	override fun parse(cursor: PzlTokenCursor, left: Expression?): SuffixUnaryExpression {
		return SuffixUnaryExpressionParser.of(cursor).parse()
	}
}