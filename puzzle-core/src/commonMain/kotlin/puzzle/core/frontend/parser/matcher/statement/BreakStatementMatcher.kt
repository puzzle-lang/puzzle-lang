package puzzle.core.frontend.parser.matcher.statement

import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.ast.statement.BreakStatement
import puzzle.core.frontend.parser.parser.statement.parseBreakStatement
import puzzle.core.frontend.token.kinds.JumpKind.BREAK

object BreakStatementMatcher : StatementMatcher<BreakStatement> {
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(BREAK)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(): BreakStatement {
		return parseBreakStatement()
	}
}