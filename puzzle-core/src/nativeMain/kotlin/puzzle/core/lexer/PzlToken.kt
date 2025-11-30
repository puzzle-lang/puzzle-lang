package puzzle.core.lexer

import puzzle.core.LineColumn
import puzzle.core.PzlContext
import puzzle.core.getLineColumn
import puzzle.core.parser.ast.TokenRange

class PzlToken(
	val type: PzlTokenType,
	val value: String,
	val range: TokenRange
) {
	private var _lineColumn: LineColumn? = null
	
	context(_: PzlContext)
	val lineColumn: LineColumn
		get() = _lineColumn ?: getLineColumn(range.start).also { _lineColumn = it }
}