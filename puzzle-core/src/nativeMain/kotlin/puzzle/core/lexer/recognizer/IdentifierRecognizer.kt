package puzzle.core.lexer.recognizer

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlToken
import puzzle.core.lexer.PzlTokenType

data object IdentifierRecognizer : TokenRecognizer {
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int, line: Int, column: Int): PzlToken? {
		val char = input[start]
		if (!char.isEnglishLetter() && char != '_') return null
		var position = start + 1
		while (position < input.size && (input[position] == '_' || input[position].isEnglishLetter() || input[position].isDigit())) {
			position++
		}
		val identifier = input.concatToString(start, position)
		var isValid = identifier == "_"
		for (c in identifier) {
			if (isValid) break
			isValid = c != '_'
		}
		if (!isValid) {
			syntaxError("不合法的标识符", line, column)
		}
		return PzlToken(PzlTokenType.IDENTIFIER, identifier, start, position, line, column)
	}
	
	private fun Char.isEnglishLetter(): Boolean {
		return this in 'a'..'z' || this in 'A'..'Z'
	}
}