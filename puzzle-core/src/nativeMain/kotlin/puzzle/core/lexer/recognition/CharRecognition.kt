package puzzle.core.lexer.recognition

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.token.PzlToken
import puzzle.core.token.kinds.LiteralKind
import puzzle.core.model.span
import puzzle.core.util.EscapeType
import puzzle.core.util.isHex

object CharRecognition : TokenRecognition {
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		if (input[start] != '\'') return null
		if (start + 2 < input.size && input[start + 2] == '\'' && input[start + 1] != '\\') {
			val value = input[start + 1].toString()
			return PzlToken(LiteralKind.Char(value), start span start + 3)
		}
		if (start + 3 < input.size && input[start + 3] == '\'' && input[start + 1] == '\\') {
			val escape = input[start + 2]
			if (escape in EscapeType.standardEscapes) {
				val kind = LiteralKind.Char("\\$escape")
				return PzlToken(kind, start span start + 4)
			} else {
				syntaxError("非法转义字符: '\\$escape'", start + 1)
			}
		}
		if (start + 7 < input.size && input[start + 7] == '\'' && input[start + 1] == '\\' && input[start + 2] == EscapeType.UNICODE.value) {
			val unicode = input.concatToString(start + 3, start + 7)
			if (!unicode.isHex()) {
				syntaxError("非法转义字符: '\\u$unicode'", start + 1)
			}
			val kind = LiteralKind.Char("\\u$unicode")
			return PzlToken(kind, start span start + 8)
		}
		syntaxError("字符语法错误", start + 1)
	}
}