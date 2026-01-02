package puzzle.core.frontend.parser.matcher.statement

import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.ast.statement.ContextualStatement
import puzzle.core.frontend.parser.parser.statement.parseContextualStatement
import puzzle.core.frontend.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.frontend.token.kinds.ContextualKind.SUPER
import puzzle.core.frontend.token.kinds.ContextualKind.THIS

object ContextualStatementMatcher : StatementMatcher<ContextualStatement> {
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(THIS, LPAREN) || cursor.match(SUPER, LPAREN)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(): ContextualStatement {
		return parseContextualStatement()
	}
}