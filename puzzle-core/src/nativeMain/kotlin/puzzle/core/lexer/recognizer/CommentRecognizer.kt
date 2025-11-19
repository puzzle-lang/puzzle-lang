package puzzle.core.lexer.recognizer

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlToken
import puzzle.core.lexer.PzlTokenType

data object CommentRecognizer : TokenRecognizer {
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int, line: Int, column: Int): PzlToken? {
		if (start + 1 >= input.size || input[start] != '/') return null
		return when (input[start + 1]) {
			'/' -> parseSingleComment(input, start, line, column)
			'*' -> parseMultiComment(input, start, line, column)
			else -> null
		}
	}
	
	private fun parseSingleComment(input: CharArray, start: Int, line: Int, column: Int): PzlToken {
		var position = start + 2
		while (position < input.size && input[position] != '\n') {
			position++
		}
		val comment = input.concatToString(start + 1, position).trim()
		return PzlToken(PzlTokenType.SINGLE_COMMENT, comment, start, position, line, column)
	}
	
	context(_: PzlContext)
	private fun parseMultiComment(input: CharArray, start: Int, line: Int, column: Int): PzlToken {
		var position = start + 2
		var column = column + 2
		var line = line
		var isEnd = false
		while (position + 1 < input.size) {
			when (input[position]) {
				'*' if input[position + 1] == '/' -> {
					isEnd = true
					break
				}
				
				'\n' -> {
					line++
					position++
					column = 0
				}
				
				else -> {
					position++
					column++
				}
			}
		}
		if (!isEnd) {
			syntaxError("多行注释未结束", line, column)
		}
		val comment = input.concatToString(start + 2, position)
			.split("\n")
			.joinToString("\n") {
				it.trim().trimStart('*').trimStart()
			}.trim('\n')
		return PzlToken(PzlTokenType.MULTI_COMMENT, comment, start, position + 2, line, column)
	}
}