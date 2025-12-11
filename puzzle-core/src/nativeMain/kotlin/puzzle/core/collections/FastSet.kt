package puzzle.core.collections

class FastSet<T>(
	val elements: Array<T>
) : Set<T> {
	
	override val size = elements.size
	
	private val hashSet = elements.toHashSet()
	
	override fun isEmpty(): Boolean {
		return elements.isEmpty()
	}
	
	override fun contains(element: T): Boolean {
		return element in hashSet
	}
	
	override fun iterator(): Iterator<T> {
		return elements.iterator()
	}
	
	override fun containsAll(elements: Collection<T>): Boolean {
		return elements.all { it in hashSet }
	}
	
	inline fun fastForEach(action: (T) -> Unit) {
		val arr = elements
		for (i in arr.indices) {
			action(arr[i])
		}
	}
	
	inline fun fastForEachIndexed(action: (index: Int, T) -> Unit) {
		val arr = elements
		for (i in arr.indices) {
			action(i, arr[i])
		}
	}
}

inline fun <reified T> fastSetOf(vararg elements: T): FastSet<T> {
	return FastSet(elements.toSet().toTypedArray())
}

inline fun <reified T> Set<T>.toFastSet(): FastSet<T> {
	return FastSet(this.toTypedArray())
}

inline fun <reified T> List<T>.toFastSet(): FastSet<T> {
	return fastSetOf(*this.toTypedArray())
}

inline fun <reified T> mergeFastSets(
	set1: FastSet<out T>,
	set2: FastSet<out T>,
	vararg sets: FastSet<out T>
): FastSet<T> {
	val tokenKinds = buildSet<T> {
		this += set1.elements
		this += set2.elements
		sets.forEach {
			this += it.elements
		}
	}
	return tokenKinds.toFastSet()
}