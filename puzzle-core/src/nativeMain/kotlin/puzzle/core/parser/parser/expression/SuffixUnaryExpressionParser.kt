package puzzle.core.parser.parser.expression

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.IdentifierExpression
import puzzle.core.parser.ast.expression.SuffixUnaryExpression
import puzzle.core.symbol.toOperator

class SuffixUnaryExpressionParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<SuffixUnaryExpressionParser>(::SuffixUnaryExpressionParser)
	
	context(_: PzlContext)
	fun parse(): SuffixUnaryExpression {
		cursor.expect(PzlTokenType.IDENTIFIER, "一元运算符前必须跟标识符")
		val expression = IdentifierExpression(cursor.previous.value)
		cursor.advance()
		val operator = cursor.previous.type.toOperator()
		return SuffixUnaryExpression(expression, operator)
	}
}