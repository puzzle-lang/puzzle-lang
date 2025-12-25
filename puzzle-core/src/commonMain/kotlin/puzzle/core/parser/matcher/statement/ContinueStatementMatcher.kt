package puzzle.core.parser.matcher.statement

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.ContinueStatement
import puzzle.core.parser.parser.statement.parseContinueStatement
import puzzle.core.token.kinds.JumpKind.CONTINUE

object ContinueStatementMatcher : StatementMatcher<ContinueStatement> {
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(CONTINUE)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(): ContinueStatement {
		return parseContinueStatement()
	}
}