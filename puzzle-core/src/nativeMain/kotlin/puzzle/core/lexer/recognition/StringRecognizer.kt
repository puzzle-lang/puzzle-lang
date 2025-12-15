package puzzle.core.lexer.recognition

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.token.LiteralKind
import puzzle.core.token.PzlToken
import puzzle.core.util.EscapeType
import puzzle.core.util.isHex

object StringRecognition : TokenRecognition {
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		if (input[start] != '"') return null
		val sb = StringBuilder()
		var position = start + 1
		var close = false
		while (position < input.size) {
			when (val c = input[position]) {
				'\\' -> {
					when (val escape = input[position + 1]) {
						in EscapeType.standardEscapes -> {
							sb.append(escape)
							position += 2
						}
						
						EscapeType.UNICODE.value -> {
							val unicode = input.concatToString(position + 2, position + 6)
							if (!unicode.isHex()) {
								syntaxError("非法转义字符: '\\u$unicode'", position)
							}
							sb.append("\\u$unicode")
							position += 6
						}
						
						else -> {
							syntaxError("非法转义字符: '\\$escape'", position)
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
		val kind = LiteralKind.String(sb.toString())
		return PzlToken(kind, start, position)
	}
}