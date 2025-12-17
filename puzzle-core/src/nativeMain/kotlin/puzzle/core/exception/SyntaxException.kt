package puzzle.core.exception

import puzzle.core.model.PzlContext
import puzzle.core.token.PzlToken
import puzzle.core.model.SourcePosition
import puzzle.core.model.calcPosition

private class SyntaxException(
	message: String,
	sourcePath: String,
	position: SourcePosition,
	token: PzlToken?,
) : Exception("错误位置: $sourcePath:$position${getTokenInfo(token)}$message.")

private fun getTokenInfo(token: PzlToken?): String {
	if (token == null) return " "
	return " >> ${token.value} << "
}

context(context: PzlContext)
fun syntaxError(message: String, position: Int): Nothing {
	val position = calcPosition(position)
	throw SyntaxException(message, context.sourcePath, position, null)
}

context(context: PzlContext)
fun syntaxError(message: String, token: PzlToken): Nothing {
	val position = token.location.startPosition
	throw SyntaxException(message, context.sourcePath, position, token)
}