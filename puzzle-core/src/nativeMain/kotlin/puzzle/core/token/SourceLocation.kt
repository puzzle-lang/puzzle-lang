package puzzle.core.token

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import puzzle.core.model.PzlContext

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

@Suppress("NOTHING_TO_INLINE")
inline infix fun Int.span(end: Int): SourceLocation {
	return SourceLocation(this, end)
}

@Suppress("NOTHING_TO_INLINE")
inline infix fun SourceLocation.span(other: SourceLocation): SourceLocation {
	return SourceLocation(this.start, other.start)
}

fun SourceLocation.copy(
	start: (SourceLocation) -> Int = { this.start },
	end: (SourceLocation) -> Int = { this.end },
): SourceLocation {
	val start = start(this)
	val end = end(this)
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