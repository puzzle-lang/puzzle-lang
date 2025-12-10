package puzzle.core.parser.matcher.statement

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.Statement

sealed interface StatementMatcher<S : Statement> {
	
	companion object {
		
		val matchers = arrayOf(
			VariableDeclarationStatementMatcher,
			IfStatementMatcher,
			ExpressionStatementMatcher
		)
	}
	
	context(cursor: PzlTokenCursor)
	fun match(): Boolean
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	fun parse(): S
}