package puzzle.core.lexer.recognition

import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlLexer
import puzzle.core.lexer.recognition.StringRecognition.CharType.*
import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.token.PzlToken
import puzzle.core.token.kinds.MetaKind
import puzzle.core.token.kinds.StringKind
import puzzle.core.util.isHex
import puzzle.core.util.isIdentifierStart
import puzzle.core.util.safeString

object StringRecognition : TokenRecognition {
	
	private val standardEscapeCodes = setOf('\'', '"', '\\', 'n', 'r', 't', 'b', 'f')
	
	private const val UNICODE_CODE = 'u'
	
	private const val TRIPLE_QUOTATION = """""""""
	
	private const val QUOTATION = '\"'
	
	private const val DOLLAR = '$'
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		val dollarCount = when (input[start]) {
			QUOTATION -> 0
			DOLLAR -> {
				var count = 1
				while (input[start + count] == DOLLAR) {
					count++
				}
				if (input[start + count] == QUOTATION) count else return null
			}
			
			else -> return null
		}
		val isMultiLine = input.safeString(start + dollarCount, 3) == TRIPLE_QUOTATION
		val isText = dollarCount == 0
		return when {
			isText && isMultiLine -> parseMultiLineText(input, start)
			isText -> parseSingleLineText(input, start)
			isMultiLine -> parseMultiLineTemplate(input, start, dollarCount)
			else -> parseSingleLineTemplate(input, start, dollarCount)
		}
	}
	
	context(_: PzlContext)
	private fun parseSingleLineText(input: CharArray, start: Int): PzlToken {
		var position = start + 1
		while (position < input.size) {
			position += when (parseCharType(input, position, false)) {
				NORMAL -> 1
				ESCAPE -> 2
				UNICODE -> 6
				else -> break
			}
		}
		val kind = StringKind.Text(input.concatToString(start + 1, position))
		return PzlToken(kind, start span position + 1)
	}
	
	context(_: PzlContext)
	private fun parseMultiLineText(input: CharArray, start: Int): PzlToken {
		var position = start + 3
		while (position < input.size) {
			position += when (parseCharType(input, position, true)) {
				NORMAL -> 1
				ESCAPE -> 2
				UNICODE -> 6
				else -> break
			}
		}
		val value = input.concatToString(start + 3, position).trimIndent()
		val kind = StringKind.Text(value)
		return PzlToken(kind, start span position + 3)
	}
	
	context(_: PzlContext)
	private fun parseSingleLineTemplate(input: CharArray, start: Int, dollarCount: Int): PzlToken {
		var textStart = start + dollarCount + 1
		var position = textStart
		val parts = mutableListOf<StringKind.Template.Part>()
		while (position < input.size) {
			val type = parseCharType(input, position, false, dollarCount)
			position += when (type) {
				NORMAL -> 1
				ESCAPE -> 2
				UNICODE -> 6
				END -> {
					if (textStart < position) {
						val value = input.concatToString(textStart, position)
						parts += StringKind.Template.Part.Text(value, textStart span position)
					}
					break
				}
				
				else -> {
					if (textStart < position) {
						val value = input.concatToString(textStart, position)
						parts += StringKind.Template.Part.Text(value, textStart span position)
					}
					when (type) {
						SIMPLE_INTERPOLATION -> {
							val start = position
							position += dollarCount
							val token = IdentifierRecognition.tryParse(input, position)!!
							position += token.value.length
							val tokens = listOf(token, PzlToken(MetaKind.EOF, token.location.end span token.location.end + 1))
							parts += StringKind.Template.Part.Expression(tokens, start span position)
							textStart = position
						}
						
						EXPRESSION_INTERPOLATION -> {
							val start = position
							position += dollarCount + 1
							val tokens = PzlLexer.templateExpression(input, position).scan()
							if (tokens.isEmpty()) {
								syntaxError("字符串插值缺少表达式", position)
							}
							val tokensEnd = tokens.last().location.end
							position = tokensEnd
							parts += StringKind.Template.Part.Expression(tokens, start span position)
							textStart = position
						}
					}
					continue
				}
			}
		}
		val kind = StringKind.Template(parts)
		return PzlToken(kind, start span position + 1)
	}
	
	context(_: PzlContext)
	private fun parseMultiLineTemplate(input: CharArray, start: Int, dollarCount: Int): PzlToken {
		error("暂不支持多行字符串模版")
	}
	
	context(_: PzlContext)
	private fun parseCharType(
		input: CharArray,
		position: Int,
		isMultiLine: Boolean,
		dollarCount: Int = 0,
	): CharType {
		return when (input[position]) {
			'\\' -> {
				if (position + 1 >= input.size) {
					syntaxError("语法错误", position)
				}
				when (val escape = input[position + 1]) {
					in standardEscapeCodes -> ESCAPE
					
					UNICODE_CODE -> {
						val unicode = input.safeString(position + 2, 4)
							?: syntaxError("语法错误", position)
						if (!unicode.isHex()) {
							syntaxError("非法转译字符: '${input.safeString(position, 6)}'", position)
						}
						UNICODE
					}
					
					else -> syntaxError("非法转译字符: '\\$escape'", position)
				}
			}
			
			'\n' if !isMultiLine -> syntaxError("请使用多行字符串", position)
			
			QUOTATION if (!isMultiLine || (input.safeString(position, 3) == TRIPLE_QUOTATION && input.getOrNull(position + 3) != QUOTATION)) -> END
			DOLLAR if dollarCount > 0 -> {
				val dollars = input.safeString(position, dollarCount) ?: return NORMAL
				if (dollars.any { it == DOLLAR }) {
					val char = input.getOrNull(position + dollarCount) ?: return NORMAL
					when {
						char == '{' -> EXPRESSION_INTERPOLATION
						char.isIdentifierStart() -> SIMPLE_INTERPOLATION
						else -> NORMAL
					}
				} else NORMAL
			}
			
			else -> NORMAL
		}
	}
	
	enum class CharType {
		NORMAL,
		ESCAPE,
		UNICODE,
		SIMPLE_INTERPOLATION,
		EXPRESSION_INTERPOLATION,
		END
	}
}