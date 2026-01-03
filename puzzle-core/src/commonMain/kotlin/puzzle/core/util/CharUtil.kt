package puzzle.core.util

import puzzle.core.frontend.token.kinds.KeywordKind

private val hexChars = "0123456789ABCDEFabcdef".toSet()

fun String.isHex(): Boolean {
	return this.all { it.isHex() }
}

fun Char.isHex(): Boolean = this in hexChars

private val decimalChars = "0123456789".toSet()

fun String.isDecimal(): Boolean {
	return this.all { it.isDecimal() }
}

fun Char.isDecimal(): Boolean = this in decimalChars

private val binaryChars = "01".toSet()

fun String.isBinary(): Boolean {
	return this.all { it.isBinary() }
}

fun Char.isBinary(): Boolean = this in binaryChars

fun Char.isIdentifierStart(): Boolean {
	return this in 'a'..'z' || this in 'A'..'Z' || this == '_'
}

fun Char.isIdentifierPart(): Boolean {
	return this in 'a'..'z' || this in 'A'..'Z' || this == '_' || this in '0'..'9'
}

private val keywords = KeywordKind.kinds.map { it.value }.toSet()

fun String.isIdentifierString(): Boolean {
	if (this.isEmpty()) return false
	if (this in keywords) return false
	val first = first()
	if (!first.isIdentifierStart()) return false
	return this.drop(1).all { it.isIdentifierPart() }
}

fun CharArray.safeString(start: Int, length: Int): String? {
	if (start < 0 || length <= 0 || start >= size) return null
	val end = minOf(start + length, size)
	return this.concatToString(start, end)
}