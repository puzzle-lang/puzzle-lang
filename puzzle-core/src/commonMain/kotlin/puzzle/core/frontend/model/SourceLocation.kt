package puzzle.core.frontend.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import puzzle.core.frontend.token.PzlToken

@Serializable
class SourceLocation(
	val start: Int,
	val end: Int,
) {
	
	@Transient
	private var _startPosition: SourcePosition? = null
	
	@Transient
	private var _endPosition: SourcePosition? = null
	
	context(_: PzlContext)
	val startPosition: SourcePosition
		get() = _startPosition ?: calcPosition(start).also { _startPosition = it }
	
	context(_: PzlContext)
	val endPosition: SourcePosition
		get() = _endPosition ?: calcPosition(end).also { _endPosition = it }
}

context(_: PzlContext)
fun PzlToken.equalsLine(token: PzlToken): Boolean {
	return this.location.endPosition.line == token.location.startPosition.line
}

context(_: PzlContext)
fun PzlToken.notEqualsLine(token: PzlToken): Boolean {
	return this.location.endPosition.line != token.location.startPosition.line
}

@Suppress("NOTHING_TO_INLINE")
inline infix fun Int.span(end: Int): SourceLocation {
	return SourceLocation(this, end)
}

@Suppress("NOTHING_TO_INLINE")
inline infix fun SourceLocation.span(other: SourceLocation): SourceLocation {
	return SourceLocation(this.start, other.end)
}

fun SourceLocation.copy(
	start: (Int) -> Int = { this.start },
	end: (Int) -> Int = { this.end },
): SourceLocation {
	val start = start(this.start)
	val end = end(this.end)
	if (start == this.end && end == this.end) {
		return this
	}
	return SourceLocation(start, end)
}

fun SourceLocation.copy(
	start: Int = this.start,
	end: Int = this.end,
): SourceLocation {
	if (start == this.end && end == this.end) {
		return this
	}
	return SourceLocation(start, end)
}

context(context: PzlContext)
fun calcPosition(position: Int): SourcePosition {
	val line = context.lineStarts.indexOfLast { position >= it }
	val column = position - context.lineStarts[line]
	return SourcePosition(line + 1, column + 1)
}

class SourcePosition(
	val line: Int,
	val column: Int,
) {
	
	override fun toString(): String {
		return "$line:$column"
	}
}