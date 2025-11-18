package puzzle.core.lexer.recognizer

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlToken
import puzzle.core.lexer.PzlTokenType

data object CommentRecognizer : TokenRecognizer {
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int, line: Int, column: Int): PzlToken? {
		if (input[start] != '#') return null
		var position = start + 1
		while (position < input.size && input[position] != '\n') {
			position++
		}
		val comment = input.concatToString(start + 1, position).trim()
		return PzlToken(PzlTokenType.COMMENT, comment, start, position, line, column)
	}
}