package puzzle.core.parser.parser.statement

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.Assignment
import puzzle.core.parser.ast.statement.AssignmentStatement
import puzzle.core.parser.parser.expression.parseExpressionChain
import puzzle.core.parser.parser.expression.toIdentifier
import puzzle.core.token.kinds.AssignmentKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseAssignmentStatement(): AssignmentStatement {
	val (start, target) = cursor.offset(-2).let { it.location to it.toIdentifier() }
	val assignment = cursor.previous.let { Assignment(it.kind as AssignmentKind, it.location) }
	val value = parseExpressionChain()
	val end = cursor.previous.location
	return AssignmentStatement(target, assignment, value, start span end)
}