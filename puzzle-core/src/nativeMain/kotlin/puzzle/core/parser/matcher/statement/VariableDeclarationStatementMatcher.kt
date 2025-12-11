package puzzle.core.parser.matcher.statement

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.VariableDeclarationStatement
import puzzle.core.parser.parser.statement.parseVariableDeclarationStatement
import puzzle.core.token.ModifierKind

object VariableDeclarationStatementMatcher : StatementMatcher<VariableDeclarationStatement> {
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(ModifierKind.VAR) || cursor.match(ModifierKind.VAL)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(): VariableDeclarationStatement {
		return parseVariableDeclarationStatement()
	}
}