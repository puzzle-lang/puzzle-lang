package puzzle.core.frontend.parser.matcher.statement

import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.ast.statement.Statement

sealed interface StatementMatcher<S : Statement> {
	
	companion object {
		
		val matchers = arrayOf(
			VariableDeclarationStatementMatcher,
			ContextualStatementMatcher,
			InitStatementMatcher,
			ReturnStatementMatcher,
			BreakStatementMatcher,
			ContinueStatementMatcher,
			IfStatementMatcher,
			ForStatementMatcher,
			WhileStatementMatcher,
			OtherStatementMatcher
		)
	}
	
	context(cursor: PzlTokenCursor)
	fun match(): Boolean
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	fun parse(): S
}