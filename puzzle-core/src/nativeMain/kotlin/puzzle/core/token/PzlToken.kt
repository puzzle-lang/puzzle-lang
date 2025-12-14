package puzzle.core.token

import puzzle.core.model.PzlContext
import puzzle.core.parser.ast.TokenRange

class PzlToken(
	val kind: PzlTokenKind,
	val range: TokenRange
) {
	private var _lineColumn: LineColumn? = null
	
	context(_: PzlContext)
	val lineColumn: LineColumn
		get() = _lineColumn ?: LineColumn.get(range.start).also { _lineColumn = it }
	
	val value: String
		get() = this.kind.value
}

class LineColumn private constructor(
	val line: Int,
	val column: Int
) {
	
	companion object {
		
		context(context: PzlContext)
		fun get(position: Int): LineColumn {
			val line = context.lineStarts.indexOfLast { position >= it }
			val column = position - context.lineStarts[line]
			return LineColumn(line + 1, column + 1)
		}
	}
	
	override fun toString(): String {
		return "$line:$column"
	}
}