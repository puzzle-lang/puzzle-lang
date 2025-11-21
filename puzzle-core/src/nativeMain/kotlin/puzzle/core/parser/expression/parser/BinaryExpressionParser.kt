package puzzle.core.parser.expression.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.parser.Associativity
import puzzle.core.parser.Operator
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.expression.BinaryExpression
import puzzle.core.parser.expression.Expression
import puzzle.core.parser.expression.matcher.parseExpression
import puzzle.core.parser.toOperator

class BinaryExpressionParser(
	private val cursor: PzlTokenCursor
) {
	
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