package puzzle.core.exception

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlToken
import puzzle.core.lexer.PzlTokenType

private class SyntaxException(
	message: String,
	sourcePath: String,
	line: Int,
	column: Int,
	type: PzlTokenType?
) : Exception("错误位置: $sourcePath:$line:$column ${if (type != null) ">> $type << " else ""}$message.")

context(context: PzlContext)
fun syntaxError(message: String, line: Int, column: Int, type: PzlTokenType? = null): Nothing {
	throw SyntaxException(message, context.sourcePath, line, column, type)
}

context(context: PzlContext)
fun syntaxError(message: String, token: PzlToken): Nothing {
	throw SyntaxException(message, context.sourcePath, token.line, token.column, token.type)
}