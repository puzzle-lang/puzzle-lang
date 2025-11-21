package puzzle.core.parser.expression.parser

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.expression.IdentifierExpression
import puzzle.core.parser.expression.SuffixUnaryExpression
import puzzle.core.parser.toOperator

class SuffixUnaryExpressionParser(
	private val cursor: PzlTokenCursor
) {
	
	context(_: PzlContext)
	fun parse(): SuffixUnaryExpression {
		cursor.expect(PzlTokenType.IDENTIFIER, "一元运算符前必须跟标识符")
		val expression = IdentifierExpression(cursor.previous.value)
		cursor.advance()
		val operator = cursor.previous.type.toOperator()
		return SuffixUnaryExpression(expression, operator)
	}
}