package puzzle.core.util

fun CharArray.startsWith(prefix: String, startIndex: Int = 0): Boolean {
	if (startIndex < 0 || startIndex + prefix.length > this.size) return false
	prefix.forEachIndexed { index, char ->
		if (this[startIndex + index] != char) return false
	}
	return true
}