package puzzle.core.exception

import puzzle.core.model.PzlContext
import puzzle.core.token.LineColumn
import puzzle.core.token.PzlToken

private class SyntaxException(
	message: String,
	sourcePath: String,
	lineColumn: LineColumn,
	token: PzlToken?
) : Exception("错误位置: $sourcePath:$lineColumn${getTokenInfo(token)}$message.")

private fun getTokenInfo(token: PzlToken?): String {
	if (token == null) return ""
	return " >> ${token.value} << "
}

context(context: PzlContext)
fun syntaxError(message: String, position: Int): Nothing {
	val lineColumn = LineColumn.get(position)
	throw SyntaxException(message, context.sourcePath, lineColumn, null)
}

context(context: PzlContext)
fun syntaxError(message: String, token: PzlToken): Nothing {
	val lineColumn = token.lineColumn
	throw SyntaxException(message, context.sourcePath, lineColumn, token)
}