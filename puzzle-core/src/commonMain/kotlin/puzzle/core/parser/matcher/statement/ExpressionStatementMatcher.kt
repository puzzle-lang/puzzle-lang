package puzzle.core.parser.matcher.statement

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.ExpressionStatement
import puzzle.core.parser.parser.statement.parseExpressionStatement

object ExpressionStatementMatcher : StatementMatcher<ExpressionStatement> {
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean = true
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(): ExpressionStatement {
		return parseExpressionStatement()
	}
}