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
	private var _length: Int? = null
	private var _lineColumn: LineColumn? = null
	private var _value: String? = null
	
	val length: Int
		get() = _length ?: (range.end - range.start).also { _length = it }
	
	context(_: PzlContext)
	val lineColumn: LineColumn
		get() = _lineColumn ?: getLineColumn(range.start).also { _lineColumn = it }
}