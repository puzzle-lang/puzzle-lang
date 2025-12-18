package puzzle.core.parser.matcher.statement

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.BreakStatement
import puzzle.core.parser.parser.statement.parseBreakStatement
import puzzle.core.token.kinds.JumpKind.BREAK

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