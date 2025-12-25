package puzzle.core.lexer.recognition

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.token.PzlToken
import puzzle.core.token.kinds.SymbolKind
import puzzle.core.util.safeString

object SymbolRecognition : TokenRecognition {
	
	private val symbols = SymbolKind.kinds
		.groupBy { it.value.length }
		.mapValues { (_, value) -> value.associateBy { it.value } }
	
	private val maxLength = symbols.keys.maxOf { it }
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		if (input[start].toString() !in symbols[1]!!) return null
		for (length in maxLength downTo 1) {
			if (start + length > input.size) continue
			val symbol = input.safeString(start, length)
			val kind = symbols[length]!![symbol] ?: continue
			return PzlToken(kind, start span start + length)
		}
		return null
	}
}