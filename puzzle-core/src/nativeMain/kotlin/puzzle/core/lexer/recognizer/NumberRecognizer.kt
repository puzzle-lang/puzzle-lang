package puzzle.core.lexer.recognizer

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlToken
import puzzle.core.lexer.PzlTokenType
import puzzle.core.util.isBinary
import puzzle.core.util.isDecimal
import puzzle.core.util.isHex
import puzzle.core.util.isOctal

data object NumberRecognizer : TokenRecognizer {
	
	private val legalEndChars = " +-*/%=><!&|^~,;:)]}\n\t".toSet()
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int, line: Int, column: Int): PzlToken? {
		if (!input[start].isDecimal()) return null
		val numberSystem = if (input[start] == '0' && start + 1 < input.size) {
			val symbol = input[start + 1]
			when (symbol) {
				'b', 'B' -> NumberSystem.BINARY
				'x', 'X' -> NumberSystem.HEX
				else -> NumberSystem.OCTAL
			}
		} else NumberSystem.DECIMAL
		var position = start + if (numberSystem == NumberSystem.BINARY || numberSystem == NumberSystem.HEX) 2 else 1
		val meta = NumberTokenMeta()
		while (position < input.size) {
			val c = input[position]
			when {
				c == '.' -> {
					if (meta.isDecimals || numberSystem != NumberSystem.DECIMAL) {
						incorrectDigitalFormat(line, column)
					}
					if (position + 1 < input.size && !input[position + 1].isDecimal()) {
						incorrectDigitalFormat(line, column)
					}
					meta.isDecimals = true
					position++
				}
				
				c == 'u' || c == 'U' -> {
					if (meta.isLong || meta.isFloat || meta.isUnsigned) {
						incorrectDigitalFormat(line, column)
					}
					meta.isUnsigned = true
					meta.isNumberFinished = true
					position++
				}
				
				c == 'L' -> {
					if (meta.isLong || meta.isFloat) {
						incorrectDigitalFormat(line, column)
					}
					meta.isLong = true
					meta.isNumberFinished = true
					position++
				}
				
				(c == 'f' || c == 'F') && numberSystem != NumberSystem.HEX -> {
					if (meta.isLong || meta.isFloat || meta.isUnsigned || numberSystem != NumberSystem.DECIMAL) {
						incorrectDigitalFormat(line, column)
					}
					meta.isFloat = true
					meta.isNumberFinished = true
					position++
				}
				
				numberSystem == NumberSystem.BINARY && c.isBinary() && !meta.isNumberFinished -> {
					position++
				}
				
				numberSystem == NumberSystem.OCTAL && c.isOctal() && !meta.isNumberFinished -> {
					position++
				}
				
				numberSystem == NumberSystem.DECIMAL && c.isDecimal() && !meta.isNumberFinished -> {
					position++
				}
				
				numberSystem == NumberSystem.HEX && c.isHex() && !meta.isNumberFinished -> {
					position++
				}
				
				c in legalEndChars -> break
				else -> {
					incorrectDigitalFormat(line, column)
				}
			}
		}
		return PzlToken(
			PzlTokenType.NUMBER,
			input.concatToString(start, position),
			start,
			position,
			line,
			column
		)
	}
	
	context(_: PzlContext)
	private fun incorrectDigitalFormat(line: Int, column: Int): Nothing {
		syntaxError("数字格式错误", line, column)
	}
}

private class NumberTokenMeta(
	var isFloat: Boolean = false,
	var isLong: Boolean = false,
	var isUnsigned: Boolean = false,
	var isDecimals: Boolean = false,
	var isNumberFinished: Boolean = false,
)

private enum class NumberSystem {
	BINARY,
	OCTAL,
	DECIMAL,
	HEX
}