package puzzle.core.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.BinaryExpression
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.token.Assoc
import puzzle.core.token.OperatorKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseBinaryExpression(left: Expression): BinaryExpression {
	val previous = cursor.previous
	val operator = previous.kind as OperatorKind
	val right = parseExpression()
	if (left !is BinaryExpression) {
		return BinaryExpression(left, operator, right)
	}
	val lastPriority = left.operator.priority
	val priority = operator.priority
	return when {
		lastPriority > priority -> BinaryExpression(left, operator, right)
		lastPriority < priority -> exchange(left, operator, right)
		operator.assoc == Assoc.LEFT -> BinaryExpression(left, operator, right)
		operator.assoc == Assoc.RIGHT -> exchange(left, operator, right)
		else -> syntaxError("没有结合性的运算符不允许连续", previous)
	}
}

private fun exchange(
	left: BinaryExpression,
	operator: OperatorKind,
	right: Expression
): BinaryExpression {
	return BinaryExpression(
		left = left.left,
		operator = left.operator,
		right = BinaryExpression(left.right, operator, right)
	)
}