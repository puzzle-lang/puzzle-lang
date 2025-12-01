package puzzle.core.lexer.recognizer

import puzzle.core.model.PzlContext
import puzzle.core.lexer.PzlToken
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.ast.TokenRange

data object NewlineRecognizer : TokenRecognizer {
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		val char = input[start]
		if (char != '\n') return null
		return PzlToken(PzlTokenType.NEWLINE, "", TokenRange(start, start + 1))
	}
}