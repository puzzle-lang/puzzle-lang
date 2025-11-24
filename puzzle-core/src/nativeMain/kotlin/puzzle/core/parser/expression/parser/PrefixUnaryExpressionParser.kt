package puzzle.core.parser.expression.parser

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType.IDENTIFIER
import puzzle.core.parser.PzlParser
import puzzle.core.parser.PzlParserProvider
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.expression.IdentifierExpression
import puzzle.core.parser.expression.PrefixUnaryExpression
import puzzle.core.parser.toOperator

class PrefixUnaryExpressionParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<PrefixUnaryExpressionParser>(::PrefixUnaryExpressionParser)
	
	context(_: PzlContext)
	fun parse(): PrefixUnaryExpression {
		val operator = cursor.previous.type.toOperator()
		cursor.expect(IDENTIFIER, "一元运算符后只允许跟标识符")
		val expression = IdentifierExpression(cursor.previous.value)
		return PrefixUnaryExpression(operator, expression)
	}
}