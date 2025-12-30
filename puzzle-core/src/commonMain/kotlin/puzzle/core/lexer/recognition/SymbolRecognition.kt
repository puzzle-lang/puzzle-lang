package puzzle.core.lexer.recognition

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.token.PzlToken
import puzzle.core.token.kinds.OperatorKind.IN
import puzzle.core.token.kinds.OperatorKind.NOT_IN
import puzzle.core.token.kinds.SymbolKind
import puzzle.core.util.safeString

object SymbolRecognition : TokenRecognition {
	
	private val symbols = SymbolKind.kinds
		.groupBy { it.value.length }
		.mapValues { (_, value) -> value.associateBy { it.value } }
	
	private val maxLength = SymbolKind.kinds.maxOf { it.value.length }
	
	private val starts = SymbolKind.kinds.map { it.value.first() }.toSet()
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		if (input[start] !in starts) return null
		for (length in maxLength downTo 1) {
			val symbol = input.safeString(start, length) ?: continue
			val kind = symbols[length]!![symbol] ?: continue
			if (symbol == IN.value || symbol == NOT_IN.value) {
				val next = input[start + length]
				if (next != ' ' && next != '\t') return null
			}
			return PzlToken(kind, start span start + length)
		}
		return null
	}
}