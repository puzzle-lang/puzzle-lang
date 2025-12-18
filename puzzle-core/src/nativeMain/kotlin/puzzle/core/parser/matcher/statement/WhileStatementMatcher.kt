package puzzle.core.parser.matcher.statement

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.WhileStatement
import puzzle.core.parser.parser.statement.parseWhileStatement
import puzzle.core.token.kinds.ControlFlowKind.DO
import puzzle.core.token.kinds.ControlFlowKind.WHILE

object WhileStatementMatcher : StatementMatcher<WhileStatement> {
	
	private val kinds = arrayOf(DO, WHILE)
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return kinds.any { cursor.match(it) }
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(): WhileStatement {
		return parseWhileStatement()
	}
}