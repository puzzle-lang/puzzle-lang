package puzzle.core.parser.parser.statement

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.MemberAccessExpression
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifier
import puzzle.core.token.kinds.AccessKind.QUESTION_DOT

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseMemberAccessExpression(receiver: Expression): MemberAccessExpression {
	val isSafe = cursor.previous.kind == QUESTION_DOT
	val name = parseIdentifier(IdentifierTarget.ACCESS_OPERATOR)
	return MemberAccessExpression(receiver, name, isSafe)
}