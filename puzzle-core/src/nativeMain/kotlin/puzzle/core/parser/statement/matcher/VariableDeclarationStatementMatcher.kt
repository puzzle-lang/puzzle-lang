package puzzle.core.parser.statement.matcher

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.statement.VariableDeclarationStatement
import puzzle.core.parser.statement.parser.VariableDeclarationStatementParser

object VariableDeclarationStatementMatcher : StatementMatcher<VariableDeclarationStatement> {
	
	override fun match(cursor: PzlTokenCursor): Boolean {
		return cursor.match(PzlTokenType.VAR) || cursor.match(PzlTokenType.VAL)
	}
	
	context(_: PzlContext)
	override fun parse(cursor: PzlTokenCursor): VariableDeclarationStatement {
		return VariableDeclarationStatementParser.of(cursor).parse()
	}
}