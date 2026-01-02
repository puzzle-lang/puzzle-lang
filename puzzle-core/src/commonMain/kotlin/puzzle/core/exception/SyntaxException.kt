package puzzle.core.exception

import kotlinx.io.files.Path
import puzzle.core.frontend.ast.AstNode
import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.model.SourcePosition
import puzzle.core.frontend.model.calcPosition
import puzzle.core.frontend.token.PzlToken
import puzzle.core.util.absolutePath

private class SyntaxException(
	message: String,
	sourcePath: Path,
	position: SourcePosition,
	token: PzlToken?,
) : Exception("错误位置: ${sourcePath.absolutePath}:$position${getTokenInfo(token)}$message.")

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