package puzzle.core.parser.matcher.statement

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.AssignmentStatement
import puzzle.core.parser.parser.expression.checkIdentifier
import puzzle.core.parser.parser.statement.parseAssignmentStatement
import puzzle.core.token.kinds.AssignmentKind

object AssignmentStatementMatcher : StatementMatcher<AssignmentStatement> {
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return if (cursor.checkIdentifier() && cursor.nextOrNull?.kind is AssignmentKind) {
			cursor.advance(2)
			true
		} else false
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(): AssignmentStatement {
		return parseAssignmentStatement()
	}
}