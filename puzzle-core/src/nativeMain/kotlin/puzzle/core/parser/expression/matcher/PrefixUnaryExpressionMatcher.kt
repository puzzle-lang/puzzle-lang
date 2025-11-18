package puzzle.core.parser.expression.matcher

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.expression.Expression
import puzzle.core.parser.expression.PrefixUnaryExpression
import puzzle.core.parser.expression.parser.PrefixUnaryExpressionParser

object PrefixUnaryExpressionMatcher : ExpressionMatcher<PrefixUnaryExpression> {
	
	private val tokenTypes = listOf(
		PzlTokenType.PLUS,
		PzlTokenType.MINUS,
		PzlTokenType.BANG,
		PzlTokenType.BIT_NOT,
		PzlTokenType.DOUBLE_PLUS,
		PzlTokenType.DOUBLE_MINUS
	)
	
	override fun match(ctx: PzlParserContext, left: Expression?): Boolean {
		val type = ctx.current.type
		if (type !in tokenTypes) return false
		return if (left == null || type != PzlTokenType.PLUS && type != PzlTokenType.MINUS) {
			ctx.advance()
			true
		} else false
	}
	
	context(_: PzlContext)
	override fun parse(ctx: PzlParserContext, left: Expression?): PrefixUnaryExpression {
		return PrefixUnaryExpressionParser(ctx).parse()
	}
}