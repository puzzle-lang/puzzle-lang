package puzzle.core.parser.matcher.statement

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.Statement

sealed interface StatementMatcher<S : Statement> {
	
	fun match(cursor: PzlTokenCursor): Boolean
	
	context(_: PzlContext)
	fun parse(cursor: PzlTokenCursor): S
}

private val matchers = arrayOf(
	VariableDeclarationStatementMatcher
)

context(_: PzlContext)
fun parseStatement(cursor: PzlTokenCursor): Statement {
	val matcher = matchers.find { it.match(cursor) }
		?: run {
			if (cursor.current.type == PzlTokenType.EOF) {
				syntaxError("函数缺少 '}'", cursor.current)
			} else {
				syntaxError("不支持的语句", cursor.current)
			}
		}
	return matcher.parse(cursor)
}