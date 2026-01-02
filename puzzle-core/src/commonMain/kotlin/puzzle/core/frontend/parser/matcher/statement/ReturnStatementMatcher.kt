package puzzle.core.frontend.parser.matcher.statement

import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.ast.statement.ReturnStatement
import puzzle.core.frontend.parser.parser.statement.parseReturnStatement
import puzzle.core.frontend.token.kinds.JumpKind.RETURN

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