package puzzle.core.lexer.recognition

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.token.kinds.CommentKind
import puzzle.core.token.PzlToken
import puzzle.core.model.span

object CommentRecognition : TokenRecognition {
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		if (start + 1 >= input.size || input[start] != '/') return null
		return when (input[start + 1]) {
			'/' -> parseSingleLineComment(input, start)
			'*' -> if (input.getOrNull(start + 2) == '*') parseDocComment(input, start) else parseMultiLineComment(input, start)
			else -> null
		}
	}
	
	private fun parseSingleLineComment(input: CharArray, start: Int): PzlToken {
		var position = start + 2
		while (position < input.size && input[position] != '\n') {
			position++
		}
		val comment = input.concatToString(start + 2, position).trim()
		val kind = CommentKind.SingleLine(comment)
		return PzlToken(kind, start span position)
	}
	
	context(_: PzlContext)
	private fun parseDocComment(input: CharArray, start: Int): PzlToken {
		var position = start + 3
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
			syntaxError("文档注释未结束", start)
		}
		val comment = input.concatToString(start + 3, position).split("\n")
			.joinToString("\n") { it.trim().trimStart('*').trimStart() }
			.trim('\n')
		val kind = CommentKind.Doc(comment)
		return PzlToken(kind, start span position + 2)
	}
	
	context(_: PzlContext)
	private fun parseMultiLineComment(input: CharArray, start: Int): PzlToken {
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
		val comment = input.concatToString(start + 2, position).split("\n")
			.joinToString("\n") { it.trim() }
			.trim('\n')
		val kind = CommentKind.MultiLine(comment)
		return PzlToken(kind, start span position + 2)
	}
}