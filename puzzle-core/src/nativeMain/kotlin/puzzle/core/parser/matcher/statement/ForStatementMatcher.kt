package puzzle.core.parser.matcher.statement

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.ForStatement
import puzzle.core.parser.parser.statement.parseForStatement
import puzzle.core.token.kinds.ControlFlowKind.FOR

object ForStatementMatcher : StatementMatcher<ForStatement> {
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(FOR)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(): ForStatement {
		return parseForStatement()
	}
}