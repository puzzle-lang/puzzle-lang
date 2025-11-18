package puzzle.core.util

fun CharArray.startsWith(prefix: String, startIndex: Int = 0): Boolean {
	if (startIndex < 0 || startIndex + prefix.length > this.size) return false
	for ((i, c) in prefix.withIndex()) {
		if (this[startIndex + i] != c) return false
	}
	return true
}