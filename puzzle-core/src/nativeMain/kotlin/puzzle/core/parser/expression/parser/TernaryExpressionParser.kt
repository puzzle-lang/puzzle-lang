package puzzle.core.parser.expression.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.expression.Expression
import puzzle.core.parser.expression.TernaryExpression
import puzzle.core.parser.expression.matcher.parseCompleteExpression

class TernaryExpressionParser(
	private val ctx: PzlParserContext
) {
	
	context(_: PzlContext)
	fun parse(condition: Expression): TernaryExpression {
		val thenExpression = parseCompleteExpression(ctx)
		if (ctx.current.type != PzlTokenType.COLON) {
			syntaxError("三元运算符缺少 ':'", ctx.current)
		}
		ctx.advance()
		val elseExpression = parseCompleteExpression(ctx)
		return TernaryExpression(condition, thenExpression, elseExpression)
	}
}