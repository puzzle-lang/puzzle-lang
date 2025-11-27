package puzzle.core.parser.parser.expression

import puzzle.core.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.IdentifierExpression
import puzzle.core.parser.ast.expression.PrefixUnaryExpression
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.parser.identifier.IdentifierNameParser
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.symbol.toOperator

class PrefixUnaryExpressionParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<PrefixUnaryExpressionParser>(::PrefixUnaryExpressionParser)
	
	context(_: PzlContext)
	fun parse(): PrefixUnaryExpression {
		val operator = cursor.previous.type.toOperator()
		val name = IdentifierNameParser.of(cursor).parse(IdentifierNameTarget.PREFIX_UNARY)
		val expression = IdentifierExpression(name)
		return PrefixUnaryExpression(operator, expression)
	}
}