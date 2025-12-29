package puzzle.core.parser.matcher.statement

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.Statement
import puzzle.core.parser.parser.statement.parseOtherStatement

object OtherStatementMatcher : StatementMatcher<Statement> {
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean = true
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(): Statement {
		return parseOtherStatement()
	}
}