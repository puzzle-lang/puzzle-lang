package puzzle.core.parser.expression.matcher

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.expression.ElvisExpression
import puzzle.core.parser.expression.Expression
import puzzle.core.parser.expression.parser.ElvisExpressionParser

object ElvisExpressionMatcher : ExpressionMatcher<ElvisExpression> {
	
	override fun match(ctx: PzlParserContext, left: Expression?): Boolean {
		return ctx.match(PzlTokenType.ELVIS)
	}
	
	context(_: PzlContext)
	override fun parse(ctx: PzlParserContext, left: Expression?): ElvisExpression {
		if (left == null) {
			syntaxError("'?:' 操作符前必须跟表达式", ctx.previous)
		}
		return ElvisExpressionParser(ctx).parse(left)
	}
	
}