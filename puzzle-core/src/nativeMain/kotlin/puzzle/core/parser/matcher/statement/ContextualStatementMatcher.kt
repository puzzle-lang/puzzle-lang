package puzzle.core.parser.matcher.statement

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.ContextualStatement
import puzzle.core.parser.parser.statement.parseContextualStatement
import puzzle.core.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.token.kinds.ContextualKind.SUPER
import puzzle.core.token.kinds.ContextualKind.THIS

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