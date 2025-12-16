package puzzle.core.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.IsExpression
import puzzle.core.parser.parser.expression.parseIsExpression
import puzzle.core.token.OperatorKind.NOT
import puzzle.core.token.TypeOperatorKind.IS

object IsExpressionMatcher : ExpressionMatcher<IsExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match(NOT, IS) || cursor.match(IS)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): IsExpression {
		val negated = cursor.offset(offset = -2) == NOT
		if (left == null) {
			syntaxError("${if (negated) "!is" else "is"} 操作符左侧未解析到表达式", cursor.current)
		}
		return parseIsExpression(left, negated)
	}
}