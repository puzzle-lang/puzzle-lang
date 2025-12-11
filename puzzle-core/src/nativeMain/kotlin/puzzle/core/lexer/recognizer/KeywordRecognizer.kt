package puzzle.core.lexer.recognizer

import puzzle.core.model.PzlContext
import puzzle.core.parser.ast.TokenRange
import puzzle.core.token.KeywordKind
import puzzle.core.token.PzlToken
import puzzle.core.util.startsWith

object KeywordRecognizer : TokenRecognizer {
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		KeywordKind.kinds.fastForEach { kind ->
			if (!input.startsWith(kind.value, start)) {
				return@fastForEach
			}
			val end = start + kind.value.length
			val nextChar = input.getOrNull(end)
			if (nextChar != null && (nextChar.isLetterOrDigit() || nextChar == '_')) {
				return@fastForEach
			}
			return PzlToken(kind, TokenRange(start, end))
		}
		return null
	}
}