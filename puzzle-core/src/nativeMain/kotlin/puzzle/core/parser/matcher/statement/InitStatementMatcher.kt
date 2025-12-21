package puzzle.core.parser.matcher.statement

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.InitStatement
import puzzle.core.parser.parser.statement.parseInitStatement
import puzzle.core.token.kinds.ContextualKind.INIT

object InitStatementMatcher : StatementMatcher<InitStatement> {
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(INIT)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(): InitStatement {
		return parseInitStatement()
	}
}