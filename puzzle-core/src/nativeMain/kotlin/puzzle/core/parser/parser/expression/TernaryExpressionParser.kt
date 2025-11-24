package puzzle.core.parser.parser.expression

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.TernaryExpression
import puzzle.core.parser.matcher.expression.parseCompleteExpression

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