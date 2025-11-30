package puzzle.core.lexer.recognizer

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlToken
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.ast.TokenRange
import puzzle.core.util.ranges.rangeTo
import puzzle.core.util.startsWith

data object KeywordRecognizer : TokenRecognizer {
	
	private val keywords = (PzlTokenType.FUN..PzlTokenType.NULL).map { it.value to it }.toTypedArray()
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		for ((keyword, type) in keywords) {
			if (!input.startsWith(keyword, start)) {
				continue
			}
			val end = start + keyword.length
			val nextChar = input.getOrNull(end)
			if (nextChar != null && (nextChar.isLetterOrDigit() || nextChar == '_')) {
				continue
			}
			return PzlToken(type, keyword, TokenRange(start, end))
		}
		return null
	}
}