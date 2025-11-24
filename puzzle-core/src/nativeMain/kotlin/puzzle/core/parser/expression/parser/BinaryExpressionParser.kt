package puzzle.core.parser.expression.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.parser.*
import puzzle.core.parser.expression.BinaryExpression
import puzzle.core.parser.expression.Expression
import puzzle.core.parser.expression.matcher.parseExpression

class BinaryExpressionParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<BinaryExpressionParser>(::BinaryExpressionParser)
	
	context(_: PzlContext)
	fun parse(left: Expression): BinaryExpression {
		val previous = cursor.previous
		val operator = previous.type.toOperator()
		val right = parseExpression(cursor)
		if (left !is BinaryExpression) {
			return BinaryExpression(left, operator, right)
		}
		val lastPriority = left.operator.priority
		val priority = operator.priority
		return when {
			lastPriority > priority -> BinaryExpression(left, operator, right)
			lastPriority < priority -> exchange(left, operator, right)
			operator.associativity == Associativity.LEFT -> BinaryExpression(
				left,
				operator,
				right
			)
			
			operator.associativity == Associativity.RIGHT -> exchange(left, operator, right)
			else -> syntaxError("没有结合性的运算符不允许连续", previous)
		}
	}
	
	private fun exchange(left: BinaryExpression, operator: Operator, right: Expression): BinaryExpression {
		return BinaryExpression(
			left = left,
			operator = left.operator,
			right = BinaryExpression(left.right, operator, right)
		)
	}
}