package puzzle.core.lexer.recognizer

import puzzle.core.model.PzlContext
import puzzle.core.token.PzlToken
import puzzle.core.token.SymbolKind

object SymbolRecognizer : TokenRecognizer {
	
	private val symbols = SymbolKind.kinds
		.groupBy { it.value.length }
		.mapValues { (_, value) -> value.associateBy { it.value } }
	
	private val maxLength = symbols.keys.maxOf { it }
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		if (input[start].toString() !in symbols[1]!!) return null
		for (length in maxLength downTo 1) {
			if (start + length > input.size) continue
			val symbol = input.concatToString(start, start + length)
			val kind = symbols[length]!![symbol] ?: continue
			return PzlToken(kind, start, start + length)
		}
		return null
	}
}