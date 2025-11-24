package puzzle.core.lexer.recognizer

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlToken
import puzzle.core.lexer.PzlTokenType

data object SymbolRecognizer : TokenRecognizer {
	
	private val tokenTypeMap = mapOf(
		3 to mapOf(
			">>>" to PzlTokenType.USHR,
			"===" to PzlTokenType.TRIPLE_EQUALS,
			"!==" to PzlTokenType.TRIPLE_NOT_EQUALS
		),
		2 to mapOf(
			"**" to PzlTokenType.DOUBLE_STAR,
			"++" to PzlTokenType.DOUBLE_PLUS,
			"--" to PzlTokenType.DOUBLE_MINUS,
			"+=" to PzlTokenType.PLUS_ASSIGN,
			"-=" to PzlTokenType.MINUS_ASSIGN,
			"*=" to PzlTokenType.STAR_ASSIGN,
			"/=" to PzlTokenType.SLASH_ASSIGN,
			"%=" to PzlTokenType.PERCENT_ASSIGN,
			"==" to PzlTokenType.EQUALS,
			"!=" to PzlTokenType.NOT_EQUALS,
			">=" to PzlTokenType.GT_EQUALS,
			"<=" to PzlTokenType.LT_EQUALS,
			"&&" to PzlTokenType.AND,
			"||" to PzlTokenType.OR,
			"<<" to PzlTokenType.SHL,
			">>" to PzlTokenType.SHR,
			"~>" to PzlTokenType.CONTAINS,
			"!>" to PzlTokenType.NOT_CONTAINS,
			"?:" to PzlTokenType.ELVIS,
			"->" to PzlTokenType.ARROW,
			".." to PzlTokenType.DOUBLE_DOT,
			"<:" to PzlTokenType.STRICT_BOUND,
			"?=" to PzlTokenType.QUESTION_ASSIGN,
			"?." to PzlTokenType.QUESTION_DOT,
			"::" to PzlTokenType.DOUBLE_COLON
		),
		1 to mapOf(
			"+" to PzlTokenType.PLUS,
			"-" to PzlTokenType.MINUS,
			"*" to PzlTokenType.STAR,
			"/" to PzlTokenType.SLASH,
			"%" to PzlTokenType.PERCENT,
			"=" to PzlTokenType.ASSIGN,
			">" to PzlTokenType.GT,
			"<" to PzlTokenType.LT,
			"!" to PzlTokenType.BANG,
			"&" to PzlTokenType.BIT_AND,
			"|" to PzlTokenType.BIT_OR,
			"^" to PzlTokenType.BIT_XOR,
			"~" to PzlTokenType.BIT_NOT,
			"." to PzlTokenType.DOT,
			"," to PzlTokenType.COMMA,
			";" to PzlTokenType.SEMICOLON,
			":" to PzlTokenType.COLON,
			"?" to PzlTokenType.QUESTION,
			"@" to PzlTokenType.AT,
			"(" to PzlTokenType.LPAREN,
			")" to PzlTokenType.RPAREN,
			"[" to PzlTokenType.LBRACKET,
			"]" to PzlTokenType.RBRACKET,
			"{" to PzlTokenType.LBRACE,
			"}" to PzlTokenType.RBRACE,
		)
	)
	
	private val maxLength = tokenTypeMap.keys.maxOf { it }
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int, line: Int, column: Int): PzlToken? {
		if (input[start].toString() !in tokenTypeMap[1]!!) return null
		for (length in maxLength downTo 1) {
			if (start + length > input.size) continue
			val symbol = input.concatToString(start, start + length)
			val tokenType = tokenTypeMap[length]!![symbol] ?: continue
			return PzlToken(tokenType, "", start, start + length, line, column)
		}
		return null
	}
}