package puzzle.core.exception

import puzzle.core.model.PzlContext
import puzzle.core.model.SourcePosition
import puzzle.core.model.calcPosition
import puzzle.core.parser.ast.AstNode
import puzzle.core.token.PzlToken

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

context(context: PzlContext)
fun syntaxError(message: String, node: AstNode): Nothing {
	val position = node.location.startPosition
	throw SyntaxException(message, context.sourcePath, position, null)
}