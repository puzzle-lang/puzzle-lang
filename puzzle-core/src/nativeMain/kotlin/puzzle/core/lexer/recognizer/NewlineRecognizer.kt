package puzzle.core.lexer.recognizer

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlToken
import puzzle.core.lexer.PzlTokenType

data object NewlineRecognizer : TokenRecognizer {
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int, line: Int, column: Int): PzlToken? {
		val char = input[start]
		if (char != '\n') return null
		return PzlToken(PzlTokenType.NEWLINE, "", start, start + 1, line, column)
	}
}