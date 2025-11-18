package puzzle.core.parser.expression.parser

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.expression.IdentifierExpression
import puzzle.core.parser.expression.SuffixUnaryExpression
import puzzle.core.parser.toOperator

class SuffixUnaryExpressionParser(
	private val ctx: PzlParserContext
) {
	
	context(_: PzlContext)
	fun parse(): SuffixUnaryExpression {
		ctx.expect(PzlTokenType.IDENTIFIER, "一元运算符前必须跟标识符")
		val expression = IdentifierExpression(ctx.previous.value)
		ctx.advance()
		val operator = ctx.previous.type.toOperator()
		return SuffixUnaryExpression(expression, operator)
	}
}