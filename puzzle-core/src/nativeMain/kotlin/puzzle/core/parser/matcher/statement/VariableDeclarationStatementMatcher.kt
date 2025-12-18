package puzzle.core.parser.matcher.statement

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.VariableDeclarationStatement
import puzzle.core.parser.parser.statement.parseVariableDeclarationStatement
import puzzle.core.token.kinds.ModifierKind.VAL
import puzzle.core.token.kinds.ModifierKind.VAR

object VariableDeclarationStatementMatcher : StatementMatcher<VariableDeclarationStatement> {
	
	private val kinds = arrayOf(VAR, VAL)
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return kinds.any { cursor.match(it) }
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(): VariableDeclarationStatement {
		return parseVariableDeclarationStatement()
	}
}