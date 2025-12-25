package puzzle.core.parser.parser.expression

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.NonNullAssertionExpression

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseNonNullAssertionExpression(receiver: Expression): NonNullAssertionExpression {
	val end = cursor.previous.location
	return NonNullAssertionExpression(receiver, receiver.location span end)
}