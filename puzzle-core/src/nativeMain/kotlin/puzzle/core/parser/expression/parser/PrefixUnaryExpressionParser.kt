package puzzle.core.parser.expression.parser

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType.IDENTIFIER
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.expression.IdentifierExpression
import puzzle.core.parser.expression.PrefixUnaryExpression
import puzzle.core.parser.toOperator

class PrefixUnaryExpressionParser(
	private val ctx: PzlParserContext
) {
	
	context(_: PzlContext)
	fun parse(): PrefixUnaryExpression {
		val operator = ctx.previous.type.toOperator()
		ctx.expect(IDENTIFIER, "一元运算符后只允许跟标识符")
		val expression = IdentifierExpression(ctx.previous.value)
		return PrefixUnaryExpression(operator, expression)
	}
}