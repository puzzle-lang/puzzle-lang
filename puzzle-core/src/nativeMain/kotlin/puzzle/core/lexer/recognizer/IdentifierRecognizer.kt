package puzzle.core.lexer.recognizer

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.token.IdentifierKind
import puzzle.core.token.PzlToken

object IdentifierRecognizer : TokenRecognizer {
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		val char = input[start]
		if (!char.isEnglishLetter() && char != '_') return null
		var position = start + 1
		while (position < input.size && (input[position] == '_' || input[position].isEnglishLetter() || input[position].isDigit())) {
			position++
		}
		val identifier = input.concatToString(start, position)
		val isValid = identifier == "_" || identifier.any { it != '_' }
		if (!isValid) {
			syntaxError("不合法的标识符", start)
		}
		val kind = IdentifierKind(identifier)
		return PzlToken(kind, start, position)
	}
	
	private fun Char.isEnglishLetter(): Boolean {
		return this in 'a'..'z' || this in 'A'..'Z'
	}
}