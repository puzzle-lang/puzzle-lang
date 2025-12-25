package puzzle.core.parser.matcher.statement

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.ReturnStatement
import puzzle.core.parser.parser.statement.parseReturnStatement
import puzzle.core.token.kinds.JumpKind.RETURN

object ReturnStatementMatcher : StatementMatcher<ReturnStatement> {
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(RETURN)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(): ReturnStatement {
		return parseReturnStatement()
	}
}