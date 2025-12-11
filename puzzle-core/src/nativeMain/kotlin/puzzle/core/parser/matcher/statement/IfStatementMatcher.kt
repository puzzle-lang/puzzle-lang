package puzzle.core.parser.matcher.statement

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.Statement
import puzzle.core.parser.parser.statement.parseIfStatement
import puzzle.core.token.ControlFlowKind

object IfStatementMatcher : StatementMatcher<Statement> {
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(ControlFlowKind.IF)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(): Statement {
		return parseIfStatement()
	}
}