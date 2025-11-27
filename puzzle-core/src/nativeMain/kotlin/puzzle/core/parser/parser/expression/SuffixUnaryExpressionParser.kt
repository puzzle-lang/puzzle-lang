package puzzle.core.parser.parser.expression

import puzzle.core.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.IdentifierExpression
import puzzle.core.parser.ast.expression.SuffixUnaryExpression
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.parser.identifier.IdentifierNameParser
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.symbol.toOperator

class SuffixUnaryExpressionParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<SuffixUnaryExpressionParser>(::SuffixUnaryExpressionParser)
	
	context(_: PzlContext)
	fun parse(): SuffixUnaryExpression {
		val name = IdentifierNameParser.of(cursor).parse(IdentifierNameTarget.SUFFIX_UNARY)
		val expression = IdentifierExpression(name)
		cursor.advance()
		val operator = cursor.previous.type.toOperator()
		return SuffixUnaryExpression(expression, operator)
	}
}