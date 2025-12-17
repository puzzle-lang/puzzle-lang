package puzzle.core.parser.parser.statement

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.Statement
import puzzle.core.parser.matcher.statement.StatementMatcher
import puzzle.core.token.kinds.BracketKind
import puzzle.core.token.kinds.MetaKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseStatement(): Statement {
	val matcher = StatementMatcher.matchers.find { it.match() }
		?: syntaxError(
			message = if (cursor.current.kind == MetaKind.EOF) "结尾缺少 '}'" else "不支持的语句",
			token = cursor.current
		)
	return matcher.parse()
}

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseStatements(): List<Statement> {
	return buildList {
		while (!cursor.match(BracketKind.End.RBRACE)) {
			this += parseStatement()
		}
	}
}