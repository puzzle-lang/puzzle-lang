package puzzle.core.lexer.recognizer

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlToken
import puzzle.core.lexer.PzlTokenType

data object EOFRecognizer : TokenRecognizer {
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int, line: Int, column: Int): PzlToken? {
		if (start < input.size) return null
		return PzlToken(PzlTokenType.EOF, "", start, start + 1, line, column)
	}
}