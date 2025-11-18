package puzzle.core.parser.expression.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.expression.*
import puzzle.core.parser.expression.matcher.parseCompleteExpression

class GroupingExpressionParser(
	private val ctx: PzlParserContext
) {
	
	context(_: PzlContext)
	fun parse(): Expression {
		var expression = parseCompleteExpression(ctx)
		if (ctx.current.type != PzlTokenType.RPAREN) {
			syntaxError("'(' 必须由 ')' 结束", ctx.current)
		}
		ctx.advance()
		expression = if (isOmissible(expression)) expression else GroupingExpression(expression)
		return if (isAccessOperator(ctx)) {
			PostfixExpressionParser(ctx).parse(expression)
		} else expression
	}
	
	private fun isOmissible(expression: Expression): Boolean {
		return expression !is BinaryExpression &&
				expression !is PrefixUnaryExpression &&
				expression !is TernaryExpression
	}
}