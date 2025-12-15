package puzzle.core.lexer.recognizer

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.token.LiteralKind
import puzzle.core.token.NumberLiteralType
import puzzle.core.token.NumberSystem
import puzzle.core.token.PzlToken
import puzzle.core.util.isBinary
import puzzle.core.util.isDecimal
import puzzle.core.util.isHex

object NumberRecognizer : TokenRecognizer {
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		val system = getNumberSystemOrNull(input, start) ?: return null
		return when (system) {
			NumberSystem.BINARY -> parseBinaryPzlToken(input, start)
			NumberSystem.DECIMAL -> parseDecimalPzlToken(input, start)
			NumberSystem.HEX -> parseHexPzlToken(input, start)
		}
	}
	
	context(_: PzlContext)
	private fun getNumberSystemOrNull(input: CharArray, start: Int): NumberSystem? {
		val first = input[start]
		if (first != '.' && !first.isDecimal()) return null
		val second = input.getOrNull(start + 1)
			?: return if (first.isDecimal()) NumberSystem.DECIMAL else null
		return when (first) {
			'.' -> if (second.isDecimal()) NumberSystem.DECIMAL else null
			'0' -> when (second) {
				'b', 'B' -> NumberSystem.BINARY
				'x', 'X' -> NumberSystem.HEX
				'.' -> {
					val third = input.getOrNull(start + 2)
						?: numberFormatError(start + 2)
					if (!third.isDecimal()) {
						numberFormatError(start + 2)
					}
					NumberSystem.DECIMAL
				}
				
				else -> NumberSystem.DECIMAL
			}
			
			else -> NumberSystem.DECIMAL
		}
	}
	
	private const val BINARY_UINT_MAX_LENGTH = 32
	private const val BINARY_ULONG_MAX_LENGTH = 64
	
	context(_: PzlContext)
	private fun parseBinaryPzlToken(input: CharArray, start: Int): PzlToken {
		var position = start + 2
		var byteSize = 4
		var isUnsigned = false
		val binary = buildString {
			while (true) {
				val char = input.getOrNull(position) ?: break
				when {
					char.isBinary() -> {
						this.append(char)
						position++
					}
					
					char == 'U' || char == 'u' -> {
						isUnsigned = true
						position++
						val nextChar = input.getOrNull(position) ?: break
						if (nextChar == 'L') {
							byteSize = 8
							position++
						}
						break
					}
					
					char == 'L' -> {
						byteSize = 8
						position++
						break
					}
					
					char == 'F' || char == 'f' -> numberFormatError(position)
					
					else -> break
				}
			}
		}
		if (binary.isEmpty()) {
			numberFormatError(position)
		}
		if (byteSize == 4 && (isUnsigned && binary.length > BINARY_UINT_MAX_LENGTH) || (!isUnsigned && binary.length >= BINARY_UINT_MAX_LENGTH)) {
			byteSize = 8
		}
		if ((isUnsigned && binary.length > BINARY_ULONG_MAX_LENGTH) || (!isUnsigned && binary.length >= BINARY_ULONG_MAX_LENGTH)) {
			numberValueIsOutOfRangeError(start)
		}
		val type = NumberLiteralType.get(isDecimal = false, isUnsigned = isUnsigned, byteSize = byteSize)
		val kind = LiteralKind.Number(binary, NumberSystem.BINARY, type)
		return PzlToken(kind, start, position)
	}
	
	private const val DECIMAL_INT_MAX = Int.MAX_VALUE.toString()
	private val DECIMAL_UINT_MAX = UInt.MAX_VALUE.toString()
	private const val DECIMAL_LONG_MAX = Long.MAX_VALUE.toString()
	private val DECIMAL_ULONG_MAX = ULong.MAX_VALUE.toString()
	
	context(_: PzlContext)
	private fun parseDecimalPzlToken(input: CharArray, start: Int): PzlToken {
		var position = start
		var isDecimal = false
		var byteSize = 4
		var isUnsigned = false
		val decimal = buildString {
			while (true) {
				val char = input.getOrNull(position) ?: break
				when {
					char.isDecimal() -> {
						this.append(char)
						position++
					}
					
					char == '.' -> {
						if (isDecimal) {
							break
						}
						val nextChar = input.getOrNull(position + 1) ?: break
						if (!nextChar.isDecimal()) break
						this.append('.')
						this.append(nextChar)
						isDecimal = true
						byteSize = 8
						position += 2
					}
					
					char == 'F' || char == 'f' -> {
						isDecimal = true
						position++
						byteSize = 4
						break
					}
					
					char == 'U' || char == 'u' -> {
						isUnsigned = true
						position++
						val nextChar = input.getOrNull(position) ?: break
						if (nextChar == 'L') {
							position++
							byteSize = 8
						}
						break
					}
					
					char == 'L' -> {
						position++
						byteSize = 8
						break
					}
					
					else -> break
				}
			}
		}
		if (decimal.isEmpty()) {
			numberFormatError(position)
		}
		val type = if (isDecimal) {
			NumberLiteralType.get(isDecimal = true, isUnsigned = isUnsigned, byteSize = byteSize)
		} else {
			if (byteSize == 4) {
				if (isUnsigned && (decimal.length > DECIMAL_UINT_MAX.length || decimal > DECIMAL_UINT_MAX)) {
					byteSize = 8
				} else if (!isUnsigned && (decimal.length > DECIMAL_INT_MAX.length || decimal > DECIMAL_INT_MAX)) {
					byteSize = 8
				}
			}
			when {
				byteSize == 4 -> NumberLiteralType.get(isDecimal = false, isUnsigned = isUnsigned, byteSize = 4)
				isUnsigned && (decimal.length > DECIMAL_ULONG_MAX.length || (decimal.length == DECIMAL_ULONG_MAX.length && decimal > DECIMAL_ULONG_MAX)) -> {
					numberValueIsOutOfRangeError(start)
				}
				
				!isUnsigned && (decimal.length > DECIMAL_LONG_MAX.length || (decimal.length == DECIMAL_LONG_MAX.length && decimal > DECIMAL_LONG_MAX)) -> {
					println(decimal)
					numberValueIsOutOfRangeError(start)
				}
				
				else -> NumberLiteralType.get(isDecimal = false, isUnsigned = isUnsigned, byteSize = 8)
			}
		}
		val kind = LiteralKind.Number(decimal, NumberSystem.DECIMAL, type)
		return PzlToken(kind, start, position)
	}
	
	private const val HEX_UINT_MAX_LENGTH = 8
	private const val HEX_ULONG_MAX_LENGTH = 16
	
	context(_: PzlContext)
	private fun parseHexPzlToken(input: CharArray, start: Int): PzlToken {
		var position = start + 2
		var byteSize = 4
		var isUnsigned = false
		val hex = buildString {
			while (true) {
				val char = input.getOrNull(position) ?: break
				when {
					char.isHex() -> {
						this.append(char)
						position++
					}
					
					char == 'U' || char == 'u' -> {
						isUnsigned = true
						position++
						val nextChar = input.getOrNull(position) ?: break
						if (nextChar == 'L') {
							byteSize = 8
							position++
						}
						break
					}
					
					char == 'L' -> {
						byteSize = 8
						position++
						break
					}
					
					char == 'F' || char == 'f' -> numberFormatError(position)
					
					else -> break
				}
			}
		}
		if (hex.isEmpty()) {
			numberFormatError(position)
		}
		if (byteSize == 4 && (isUnsigned && hex.length > HEX_UINT_MAX_LENGTH) || (!isUnsigned && hex.length >= HEX_UINT_MAX_LENGTH)) {
			byteSize = 8
		}
		if ((isUnsigned && hex.length > HEX_ULONG_MAX_LENGTH) || (!isUnsigned && hex.length >= HEX_ULONG_MAX_LENGTH)) {
			numberValueIsOutOfRangeError(start)
		}
		val type = NumberLiteralType.get(isDecimal = false, isUnsigned = isUnsigned, byteSize = byteSize)
		val kind = LiteralKind.Number(hex, NumberSystem.HEX, type)
		return PzlToken(kind, start, position)
	}
	
	context(_: PzlContext)
	private fun numberFormatError(position: Int): Nothing {
		syntaxError("数字格式错误", position)
	}
	
	context(_: PzlContext)
	private fun numberValueIsOutOfRangeError(position: Int): Nothing {
		syntaxError("数值超出范围", position)
	}
}