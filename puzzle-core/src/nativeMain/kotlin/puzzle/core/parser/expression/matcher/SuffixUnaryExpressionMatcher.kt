package puzzle.core.parser.expression.matcher

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.expression.Expression
import puzzle.core.parser.expression.SuffixUnaryExpression
import puzzle.core.parser.expression.parser.SuffixUnaryExpressionParser

object SuffixUnaryExpressionMatcher : ExpressionMatcher<SuffixUnaryExpression> {
	
	private val tokenTypes = listOf(PzlTokenType.DOUBLE_PLUS, PzlTokenType.DOUBLE_MINUS)
	
	override fun match(ctx: PzlParserContext, left: Expression?): Boolean {
		return ctx.next.type in tokenTypes
	}
	
	context(_: PzlContext)
	override fun parse(ctx: PzlParserContext, left: Expression?): SuffixUnaryExpression {
		return SuffixUnaryExpressionParser(ctx).parse()
	}
}