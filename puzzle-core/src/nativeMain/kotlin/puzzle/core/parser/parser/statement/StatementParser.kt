package puzzle.core.parser.parser.statement

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.Statement
import puzzle.core.parser.matcher.statement.StatementMatcher
import puzzle.core.token.kinds.BracketKind.End.RBRACE

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseStatement(): Statement {
	return StatementMatcher.matchers
		.first { it.match() }
		.parse()
}

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseStatements(): List<Statement> {
	return buildList {
		while (!cursor.match(RBRACE)) {
			this += parseStatement()
		}
	}
}