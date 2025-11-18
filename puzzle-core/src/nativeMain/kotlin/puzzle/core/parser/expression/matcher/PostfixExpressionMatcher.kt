package puzzle.core.parser.expression.matcher

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType.*
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.expression.Expression
import puzzle.core.parser.expression.parser.PostfixExpressionParser

object PostfixExpressionMatcher : ExpressionMatcher<Expression> {
	
	private val tokenTypes = listOf(
		NUMBER, STRING, CHAR, TRUE, FALSE, IDENTIFIER,
		THIS, SUPER, NULL,
		DOT, QUESTION_DOT, DOUBLE_COLON
	)
	
	override fun match(ctx: PzlParserContext, left: Expression?): Boolean {
		return tokenTypes.any { ctx.match(it) }
	}
	
	context(_: PzlContext)
	override fun parse(ctx: PzlParserContext, left: Expression?): Expression {
		return PostfixExpressionParser(ctx).parse(left)
	}
}