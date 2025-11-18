package puzzle.core.parser.expression.matcher

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.expression.Expression

sealed interface ExpressionMatcher<out E : Expression> {
	
	fun match(ctx: PzlParserContext, left: Expression?): Boolean
	
	context(_: PzlContext)
	fun parse(ctx: PzlParserContext, left: Expression?): E
}

private val matchers = listOf(
	GroupingExpressionMatcher,
	PrefixUnaryExpressionMatcher,
	SuffixUnaryExpressionMatcher,
	PostfixExpressionMatcher,
	BinaryExpressionMatcher,
	ElvisExpressionMatcher,
	TernaryExpressionMatcher
)

context(_: PzlContext)
fun parseExpression(ctx: PzlParserContext, left: Expression? = null): Expression {
	val matcher = matchers.find { it.match(ctx, left) }
		?: syntaxError("不支持的表达式", ctx.current)
	return matcher.parse(ctx, left)
}

context(_: PzlContext)
fun parseCompleteExpression(ctx: PzlParserContext): Expression {
	var expression: Expression? = null
	do {
		expression = parseExpression(ctx, expression)
	} while (!isAtExpressionEnd(ctx))
	return expression
}

private val endTokenTypes = setOf(PzlTokenType.SEMICOLON)

private val nonConsumerEndTokenTypes = setOf(
	PzlTokenType.RPAREN,
	PzlTokenType.RBRACKET,
	PzlTokenType.COLON,
	PzlTokenType.COMMA
)

private fun isAtExpressionEnd(ctx: PzlParserContext): Boolean {
	val exists = endTokenTypes.any { ctx.match(it) } ||
			nonConsumerEndTokenTypes.any { ctx.check(it) }
	if (exists) return true
	val previous = ctx.previous
	val current = ctx.current
	if (previous.line == current.line) return false
	return previous.line < current.line && current.type != PzlTokenType.AND && current.type != PzlTokenType.OR
}