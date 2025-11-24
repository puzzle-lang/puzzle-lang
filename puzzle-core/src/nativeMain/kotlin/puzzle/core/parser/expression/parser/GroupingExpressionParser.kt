package puzzle.core.parser.expression.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlParser
import puzzle.core.parser.PzlParserProvider
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.expression.*
import puzzle.core.parser.expression.matcher.parseCompleteExpression

class GroupingExpressionParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<GroupingExpressionParser>(::GroupingExpressionParser)
	
	context(_: PzlContext)
	fun parse(): Expression {
		var expression = parseCompleteExpression(cursor)
		if (cursor.current.type != PzlTokenType.RPAREN) {
			syntaxError("'(' 必须由 ')' 结束", cursor.current)
		}
		cursor.advance()
		expression = if (isOmissible(expression)) expression else GroupingExpression(expression)
		return if (isAccessOperator(cursor)) {
			PostfixExpressionParser.of(cursor).parse(expression)
		} else expression
	}
	
	private fun isOmissible(expression: Expression): Boolean {
		return expression !is BinaryExpression &&
				expression !is PrefixUnaryExpression &&
				expression !is TernaryExpression
	}
}