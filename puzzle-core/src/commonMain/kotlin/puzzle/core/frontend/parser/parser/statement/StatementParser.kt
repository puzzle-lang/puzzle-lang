package puzzle.core.frontend.parser.parser.statement

import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.ast.statement.Statement
import puzzle.core.frontend.parser.matcher.statement.StatementMatcher
import puzzle.core.frontend.token.kinds.BracketKind.End.RBRACE

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseStatement(): Statement {
	return StatementMatcher.matchers
		.first { it.match() }
		.parse()
}

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseStatements(): List<Statement> {
	if (cursor.match(RBRACE)) return emptyList()
	return buildList {
		do {
			this += parseStatement()
		} while (!cursor.match(RBRACE))
	}
}