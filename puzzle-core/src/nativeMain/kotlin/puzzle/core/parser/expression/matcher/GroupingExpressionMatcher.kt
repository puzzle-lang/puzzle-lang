package puzzle.core.parser.expression.matcher

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.expression.Expression
import puzzle.core.parser.expression.parser.GroupingExpressionParser

object GroupingExpressionMatcher : ExpressionMatcher<Expression> {
	
	override fun match(ctx: PzlParserContext, left: Expression?): Boolean {
		return ctx.match(PzlTokenType.LPAREN)
	}
	
	context(_: PzlContext)
	override fun parse(ctx: PzlParserContext, left: Expression?): Expression {
		return GroupingExpressionParser(ctx).parse()
	}
}