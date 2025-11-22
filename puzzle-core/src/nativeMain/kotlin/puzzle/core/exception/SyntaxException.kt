package puzzle.core.exception

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlToken
import puzzle.core.lexer.PzlTokenType.*

private class SyntaxException(
	message: String,
	sourcePath: String,
	line: Int,
	column: Int,
	token: PzlToken?
) : Exception("错误位置: $sourcePath:$line:$column ${getTokenInfo(token)}$message.")

private fun getTokenInfo(token: PzlToken?): String {
	if (token == null) return ""
	return if (token.type in setOf(IDENTIFIER, STRING, CHAR, NUMBER)) {
		">> ${token.value} << "
	} else {
		">> ${token.type.value} << "
	}
}

context(context: PzlContext)
fun syntaxError(message: String, line: Int, column: Int): Nothing {
	throw SyntaxException(message, context.sourcePath, line, column, null)
}

context(context: PzlContext)
fun syntaxError(message: String, token: PzlToken): Nothing {
	throw SyntaxException(message, context.sourcePath, token.line, token.column, token)
}