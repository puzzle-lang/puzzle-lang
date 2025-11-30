package puzzle.core.exception

import puzzle.core.LineColumn
import puzzle.core.PzlContext
import puzzle.core.getLineColumn
import puzzle.core.lexer.PzlToken
import puzzle.core.lexer.PzlTokenType.*

private class SyntaxException(
	message: String,
	sourcePath: String,
	lineColumn: LineColumn,
	token: PzlToken?
) : Exception("错误位置: $sourcePath:${lineColumn.line}:${lineColumn.column} ${getTokenInfo(token)}$message.")

private fun getTokenInfo(token: PzlToken?): String {
	if (token == null) return ""
	return when (token.type) {
		IDENTIFIER, STRING, CHAR, NUMBER -> ">> ${token.value} << "
		else -> ">> ${token.type.value} << "
	}
}

context(context: PzlContext)
fun syntaxError(message: String, position: Int): Nothing {
	val lineColumn = getLineColumn(position)
	throw SyntaxException(message, context.sourcePath, lineColumn, null)
}

context(context: PzlContext)
fun syntaxError(message: String, token: PzlToken): Nothing {
	val lineColumn = token.lineColumn
	throw SyntaxException(message, context.sourcePath, lineColumn, token)
}