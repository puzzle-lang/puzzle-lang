package puzzle.core.lexer.recognizer

import puzzle.core.model.PzlContext
import puzzle.core.lexer.PzlToken
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.ast.TokenRange
import puzzle.core.util.ranges.rangeTo

data object SymbolRecognizer : TokenRecognizer {
	
	private val symbols = (PzlTokenType.PLUS..PzlTokenType.RANGE_UNTIL)
		.groupBy { it.value.length }
		.mapValues { (_, value) -> value.associateBy { it.value } }
	
	private val maxLength = symbols.keys.maxOf { it }
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		if (input[start].toString() !in symbols[1]!!) return null
		for (length in maxLength downTo 1) {
			if (start + length > input.size) continue
			val symbol = input.concatToString(start, start + length)
			val tokenType = symbols[length]!![symbol] ?: continue
			return PzlToken(tokenType, symbol, TokenRange(start, start + length))
		}
		return null
	}
}