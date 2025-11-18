package puzzle.core.parser.expression.matcher

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.expression.Expression
import puzzle.core.parser.expression.TernaryExpression
import puzzle.core.parser.expression.parser.TernaryExpressionParser

object TernaryExpressionMatcher : ExpressionMatcher<TernaryExpression> {
	
	override fun match(ctx: PzlParserContext, left: Expression?): Boolean {
		return ctx.match(PzlTokenType.QUESTION)
	}
	
	context(_: PzlContext)
	override fun parse(ctx: PzlParserContext, left: Expression?): TernaryExpression {
		if (left == null) {
			syntaxError("三元运算符 '?' 前未解析到表达式", ctx.peek(offset = -2)!!)
		}
		return TernaryExpressionParser(ctx).parse(left)
	}
}