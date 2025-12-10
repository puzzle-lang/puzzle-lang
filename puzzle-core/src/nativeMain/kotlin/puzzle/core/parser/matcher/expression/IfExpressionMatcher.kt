package puzzle.core.parser.matcher.expression

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.IfExpression
import puzzle.core.parser.parser.expression.parseIfExpression

object IfExpressionMatcher : ExpressionMatcher<IfExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match(PzlTokenType.IF)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): IfExpression {
		return parseIfExpression()
	}
}