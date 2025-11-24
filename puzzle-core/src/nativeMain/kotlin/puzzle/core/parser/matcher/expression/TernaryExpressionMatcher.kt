package puzzle.core.parser.matcher.expression

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.TernaryExpression
import puzzle.core.parser.parser.expression.TernaryExpressionParser

object TernaryExpressionMatcher : ExpressionMatcher<TernaryExpression> {
	
	override fun match(cursor: PzlTokenCursor, left: Expression?): Boolean {
		return cursor.match(PzlTokenType.QUESTION)
	}
	
	context(_: PzlContext)
	override fun parse(cursor: PzlTokenCursor, left: Expression?): TernaryExpression {
		if (left == null) {
			syntaxError("三元运算符 '?' 前未解析到表达式", cursor.offset(offset = -2))
		}
		return TernaryExpressionParser.of(cursor).parse(left)
	}
}