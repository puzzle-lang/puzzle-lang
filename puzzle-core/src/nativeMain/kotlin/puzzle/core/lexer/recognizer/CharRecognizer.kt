package puzzle.core.lexer.recognizer

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlToken
import puzzle.core.lexer.PzlTokenType
import puzzle.core.util.EscapeType
import puzzle.core.util.isHex

data object CharRecognizer : TokenRecognizer {
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int, line: Int, column: Int): PzlToken? {
		if (input[start] != '\'') return null
		if (start + 2 < input.size && input[start + 2] == '\'' && input[start + 1] != '\\') {
			val value = input[start + 1].toString()
			return PzlToken(PzlTokenType.CHAR, value, start, start + 3, line, column)
		}
		if (start + 3 < input.size && input[start + 3] == '\'' && input[start + 1] == '\\') {
			val escape = input.concatToString(start + 1, start + 3)
			if (escape in EscapeType.standardEscapes) {
				return PzlToken(PzlTokenType.CHAR, escape, start, start + 4, line, column)
			} else {
				syntaxError("非法转义字符: '$escape'", line, column)
			}
		}
		if (start + 7 < input.size && input[start + 7] == '\'' && input.concatToString(start + 1, start + 3) == EscapeType.UNICODE.escape) {
			val unicode = input.concatToString(start + 3, start + 7)
			if (!unicode.isHex()) {
				syntaxError("非法转义字符: '\\u$unicode'", line, column)
			}
			return PzlToken(PzlTokenType.CHAR, "\\u$unicode", start, start + 8, line, column)
		}
		syntaxError("字符语法错误", line, column)
	}
}