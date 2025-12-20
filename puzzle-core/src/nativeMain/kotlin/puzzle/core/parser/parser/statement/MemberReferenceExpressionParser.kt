package puzzle.core.parser.parser.statement

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.MemberReferenceExpression
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifier

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseMemberReferenceExpression(receiver: Expression?): MemberReferenceExpression {
	val start = receiver?.location ?: cursor.previous.location
	val name = parseIdentifier(IdentifierTarget.ACCESS_OPERATOR)
	val end = cursor.previous.location
	return MemberReferenceExpression(receiver, name, start span end)
}