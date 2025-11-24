package puzzle.core.parser.expression.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlParser
import puzzle.core.parser.PzlParserProvider
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.expression.Expression
import puzzle.core.parser.expression.TernaryExpression
import puzzle.core.parser.expression.matcher.parseCompleteExpression

class TernaryExpressionParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<TernaryExpressionParser>(::TernaryExpressionParser)
	
	context(_: PzlContext)
	fun parse(condition: Expression): TernaryExpression {
		val thenExpression = parseCompleteExpression(cursor)
		if (cursor.current.type != PzlTokenType.COLON) {
			syntaxError("三元运算符缺少 ':'", cursor.current)
		}
		cursor.advance()
		val elseExpression = parseCompleteExpression(cursor)
		return TernaryExpression(condition, thenExpression, elseExpression)
	}
}