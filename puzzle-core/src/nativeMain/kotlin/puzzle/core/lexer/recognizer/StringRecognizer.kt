package puzzle.core.lexer.recognizer

import puzzle.core.model.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlToken
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.ast.TokenRange
import puzzle.core.util.EscapeType
import puzzle.core.util.isHex

data object StringRecognizer : TokenRecognizer {
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		if (input[start] != '"') return null
		val sb = StringBuilder()
		var position = start + 1
		var close = false
		while (position < input.size) {
			when (val c = input[position]) {
				'\\' -> {
					when (val escape = input.concatToString(position, position + 2)) {
						in EscapeType.standardEscapes -> {
							sb.append(escape)
							position += 2
						}
						
						EscapeType.UNICODE.escape -> {
							val unicode = input.concatToString(position + 2, position + 6)
							if (!unicode.isHex()) {
								syntaxError("非法转义字符: '\\u$unicode'", start + 1)
							}
							sb.append("\\u$unicode")
							position += 6
						}
						
						else -> {
							syntaxError("非法转义字符: '$escape'", start + 1)
						}
					}
				}
				
				'\"' -> {
					position++
					close = true
					break
				}
				
				else -> {
					sb.append(c)
					position++
				}
			}
		}
		if (!close) {
			syntaxError("字符串未闭合", start + 1)
		}
		return PzlToken(PzlTokenType.STRING, sb.toString(), TokenRange(start, position))
	}
}