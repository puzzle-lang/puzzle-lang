package puzzle.core.util.ranges

inline operator fun <reified E : Enum<E>> E.rangeTo(other: E): EnumRange<E> {
	return EnumRange(this, other, enumValues<E>())
}

class EnumRange<E : Enum<E>>(
	start: E,
	endInclusive: E,
	enums: Array<E>
) : EnumProgression<E>(start, endInclusive, enums), ClosedRange<E> {
	
	override val start: E get() = first
	
	override val endInclusive: E get() = last
	
	override fun contains(value: E): Boolean {
		return first.ordinal <= value.ordinal && value.ordinal <= last.ordinal
	}
	
	override fun isEmpty(): Boolean {
		return first.ordinal > last.ordinal
	}
}

sealed class EnumProgression<E : Enum<E>>(
	start: E,
	endInclusive: E,
	private val enums: Array<E>
) : Iterable<E> {
	
	init {
		if (start.ordinal >= endInclusive.ordinal) {
			error("Enum range cannot be lower than endInclusive.")
		}
	}
	
	val first = start
	
	val last = endInclusive
	
	override fun iterator(): Iterator<E> = EnumProgressionIterator(first, last, enums)
	
	open fun isEmpty(): Boolean = first > last
}

class EnumProgressionIterator<E : Enum<E>>(
	first: E,
	last: E,
	private val enums: Array<E>
) : EnumIterator<E>() {
	
	private val finalElement = last
	private var hasNext = first <= last
	private var next = if (hasNext) first else finalElement
	
	override fun hasNext(): Boolean = hasNext
	
	override fun nextEnum(): E {
		val value = next
		if (value == finalElement) {
			if (!hasNext) throw NoSuchElementException()
			hasNext = false
		} else {
			next = enums[next.ordinal + 1]
		}
		return value
	}
}

abstract class EnumIterator<E : Enum<E>> : Iterator<E> {
	
	final override fun next(): E = nextEnum()
	
	abstract fun nextEnum(): E
}