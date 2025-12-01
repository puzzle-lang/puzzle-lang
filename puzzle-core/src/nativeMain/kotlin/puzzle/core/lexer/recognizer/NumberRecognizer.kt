package puzzle.core.lexer.recognizer

import puzzle.core.model.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlToken
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.ast.TokenRange
import puzzle.core.util.isBinary
import puzzle.core.util.isDecimal
import puzzle.core.util.isHex
import puzzle.core.util.isOctal

data object NumberRecognizer : TokenRecognizer {
	
	private val legalEndChars = " +-*/%=><!&|^~,;:)]}\n\t".toSet()
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
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
						incorrectDigitalFormat(start)
					}
					if (position + 1 < input.size && !input[position + 1].isDecimal()) {
						incorrectDigitalFormat(start)
					}
					meta.isDecimals = true
					position++
				}
				
				c == 'u' || c == 'U' -> {
					if (meta.isLong || meta.isFloat || meta.isUnsigned) {
						incorrectDigitalFormat(start)
					}
					meta.isUnsigned = true
					meta.isNumberFinished = true
					position++
				}
				
				c == 'L' -> {
					if (meta.isLong || meta.isFloat) {
						incorrectDigitalFormat(start)
					}
					meta.isLong = true
					meta.isNumberFinished = true
					position++
				}
				
				(c == 'f' || c == 'F') && numberSystem != NumberSystem.HEX -> {
					if (meta.isLong || meta.isFloat || meta.isUnsigned || numberSystem != NumberSystem.DECIMAL) {
						incorrectDigitalFormat(start)
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
				else -> incorrectDigitalFormat(start)
			}
		}
		val value = input.concatToString(start, position)
		return PzlToken(PzlTokenType.NUMBER, value, TokenRange(start, position))
	}
	
	context(_: PzlContext)
	private fun incorrectDigitalFormat(position: Int): Nothing {
		syntaxError("数字格式错误", position)
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