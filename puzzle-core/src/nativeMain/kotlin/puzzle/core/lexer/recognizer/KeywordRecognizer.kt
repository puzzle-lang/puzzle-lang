package puzzle.core.lexer.recognizer

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlToken
import puzzle.core.lexer.PzlTokenType
import puzzle.core.util.startsWith

data object KeywordRecognizer : TokenRecognizer {
	
	private val keywordTokenTypeMap = mapOf(
		"fun" to PzlTokenType.FUN,
		"class" to PzlTokenType.CLASS,
		"unique" to PzlTokenType.UNIQUE,
		"trait" to PzlTokenType.TRAIT,
		"struct" to PzlTokenType.STRUCT,
		"enum" to PzlTokenType.ENUM,
		"annotation" to PzlTokenType.ANNOTATION,
		"extension" to PzlTokenType.EXTENSION,
		"private" to PzlTokenType.PRIVATE,
		"protected" to PzlTokenType.PROTECTED,
		"file" to PzlTokenType.FILE,
		"internal" to PzlTokenType.INTERNAL,
		"module" to PzlTokenType.MODULE,
		"public" to PzlTokenType.PUBLIC,
		"const" to PzlTokenType.CONST,
		"open" to PzlTokenType.OPEN,
		"abstract" to PzlTokenType.ABSTRACT,
		"owner" to PzlTokenType.OWNER,
		"final" to PzlTokenType.FINAL,
		"override" to PzlTokenType.OVERRIDE,
		"ignore" to PzlTokenType.IGNORE,
		"type" to PzlTokenType.TYPE,
		"reified" to PzlTokenType.REIFIED,
		"in" to PzlTokenType.IN,
		"out" to PzlTokenType.OUT,
		"context" to PzlTokenType.CONTEXT,
		"sealed" to PzlTokenType.SEALED,
		"init" to PzlTokenType.INIT,
		"delete" to PzlTokenType.DELETE,
		"var" to PzlTokenType.VAR,
		"val" to PzlTokenType.VAL,
		"args" to PzlTokenType.ARGS,
		"if" to PzlTokenType.IF,
		"else" to PzlTokenType.ELSE,
		"match" to PzlTokenType.MATCH,
		"for" to PzlTokenType.FOR,
		"while" to PzlTokenType.WHILE,
		"do" to PzlTokenType.DO,
		"loop" to PzlTokenType.LOOP,
		"return" to PzlTokenType.RETURN,
		"break" to PzlTokenType.BREAK,
		"continue" to PzlTokenType.CONTINUE,
		"as" to PzlTokenType.AS,
		"is" to PzlTokenType.IS,
		"package" to PzlTokenType.PACKAGE,
		"import" to PzlTokenType.IMPORT,
		"this" to PzlTokenType.THIS,
		"super" to PzlTokenType.SUPER,
		"true" to PzlTokenType.TRUE,
		"false" to PzlTokenType.FALSE,
		"null" to PzlTokenType.NULL,
	)
	
	private val keywords = keywordTokenTypeMap.keys
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int, line: Int, column: Int): PzlToken? {
		for (keyword in keywords) {
			if (!input.startsWith(keyword, start)) {
				continue
			}
			val end = start + keyword.length
			val nextChar = input.getOrNull(end)
			if (nextChar != null && (nextChar.isLetterOrDigit() || nextChar == '_')) {
				continue
			}
			val tokenType = keywordTokenTypeMap[keyword]!!
			return PzlToken(tokenType, "", start, end, line, column)
		}
		return null
	}
}