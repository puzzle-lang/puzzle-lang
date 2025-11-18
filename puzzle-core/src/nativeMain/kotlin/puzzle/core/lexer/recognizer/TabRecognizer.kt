package puzzle.core.lexer.recognizer

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlToken
import puzzle.core.lexer.PzlTokenType

data object TabRecognizer : TokenRecognizer {
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int, line: Int, column: Int): PzlToken? {
		if (input[start] != '\t') return null
		var position = start + 1
		while (position < input.size && input[position] == '\t') {
			position++
		}
		return PzlToken(PzlTokenType.TAB, "", start, position, line, column)
	}
}