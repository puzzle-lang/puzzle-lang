package puzzle.core.parser.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor

class VariableNameParser private constructor(
	private val cursor: PzlTokenCursor,
) : PzlParser {
	
	companion object : PzlParserProvider<VariableNameParser>(::VariableNameParser)
	
	private val softKeywords = listOf(
		PzlTokenType.PRIVATE,
		PzlTokenType.PROTECTED,
		PzlTokenType.FILE,
		PzlTokenType.INTERNAL,
		PzlTokenType.MODULE,
		PzlTokenType.PUBLIC,
		PzlTokenType.OPEN,
		PzlTokenType.ABSTRACT,
		PzlTokenType.SEALED,
		PzlTokenType.OWNER,
		PzlTokenType.FINAL,
		PzlTokenType.OVERRIDE,
		PzlTokenType.CONST,
		PzlTokenType.IGNORE,
		PzlTokenType.TYPE,
		PzlTokenType.REIFIED,
		PzlTokenType.IN,
		PzlTokenType.OUT,
		PzlTokenType.CONTEXT,
		PzlTokenType.INIT,
		PzlTokenType.DELETE,
		PzlTokenType.PACKAGE,
		PzlTokenType.IMPORT
	)
	
	context(_: PzlContext)
	fun parse(
		name: String,
		isSupportedAnonymity: Boolean = false
	): String {
		if (cursor.match(PzlTokenType.IDENTIFIER)) {
			val name = cursor.previous.value
			if (!isSupportedAnonymity && name == "_") {
				syntaxError("${name}不支持匿名", cursor.previous)
			}
			return name
		}
		softKeywords.forEach { keyword ->
			if (cursor.match(keyword)) {
				return keyword.value
			}
		}
		syntaxError("${name}缺少名称", cursor.current)
	}
}