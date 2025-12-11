package puzzle.core.lexer.recognizer

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.ast.TokenRange
import puzzle.core.token.CommentKind
import puzzle.core.token.PzlToken

object CommentRecognizer : TokenRecognizer {
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		if (start + 1 >= input.size || input[start] != '/') return null
		return when (input[start + 1]) {
			'/' -> parseSingleComment(input, start)
			'*' -> parseMultiComment(input, start)
			else -> null
		}
	}
	
	private fun parseSingleComment(input: CharArray, start: Int): PzlToken {
		var position = start + 2
		while (position < input.size && input[position] != '\n') {
			position++
		}
		val comment = input.concatToString(start + 1, position).trim()
		val kind = CommentKind.SingleLine(comment)
		return PzlToken(kind, TokenRange(start, position))
	}
	
	context(_: PzlContext)
	private fun parseMultiComment(input: CharArray, start: Int): PzlToken {
		var position = start + 2
		var isEnd = false
		while (position + 1 < input.size) {
			when (input[position]) {
				'*' if input[position + 1] == '/' -> {
					isEnd = true
					break
				}
				
				else -> position++
			}
		}
		if (!isEnd) {
			syntaxError("多行注释未结束", start)
		}
		val comment = input.concatToString(start + 2, position)
			.split("\n")
			.joinToString("\n") {
				it.trim().trimStart('*').trimStart()
			}.trim('\n')
		val kind = CommentKind.MultiLine(comment)
		return PzlToken(kind, TokenRange(start, position + 2))
	}
}