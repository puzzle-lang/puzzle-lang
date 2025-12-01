package puzzle.core.model

class LineColumn(
	val line: Int,
	val column: Int,
)

context(context: PzlContext)
fun getLineColumn(position: Int): LineColumn {
	val line = context.lineStarts.indexOfLast { position >= it }
	val column = position - context.lineStarts[line]
	return LineColumn(line + 1, column + 1)
}