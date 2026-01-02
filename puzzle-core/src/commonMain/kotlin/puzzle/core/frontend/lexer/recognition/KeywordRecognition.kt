package puzzle.core.frontend.lexer.recognition

import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.token.PzlToken
import puzzle.core.frontend.token.kinds.KeywordKind
import puzzle.core.frontend.model.span
import puzzle.core.util.startsWith

object KeywordRecognition : TokenRecognition {
	
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
			return PzlToken(kind, start span end)
		}
		return null
	}
}