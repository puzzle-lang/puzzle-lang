package puzzle.core.parser.parser.identifier

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlToken
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider

class IdentifierNameParser private constructor(
	private val cursor: PzlTokenCursor,
) : PzlParser {
	
	companion object : PzlParserProvider<IdentifierNameParser>(::IdentifierNameParser) {
		
		private val supportedTypes = listOf(
			PzlTokenType.IDENTIFIER,
			PzlTokenType.ABSTRACT,
			PzlTokenType.SEALED,
			PzlTokenType.OWNER,
			PzlTokenType.FINAL,
			PzlTokenType.CONST,
			PzlTokenType.TYPE,
			PzlTokenType.REIFIED,
			PzlTokenType.CONTEXT,
			PzlTokenType.INIT,
			PzlTokenType.DELETE,
			PzlTokenType.PACKAGE,
			PzlTokenType.IMPORT
		)
		
		fun check(token: PzlToken): Boolean {
			return supportedTypes.any { token.type == it }
		}
	}
	
	context(_: PzlContext)
	fun parse(target: IdentifierNameTarget): String {
		supportedTypes.forEach { type ->
			if (cursor.match(type)) {
				val value = cursor.previous.value
				if (type == PzlTokenType.IDENTIFIER && value == "_" && !target.isSupportedAnonymity) {
					syntaxError(target.notSupportedAnonymityMessage, cursor.previous)
				}
				return cursor.previous.value
			}
		}
		syntaxError(target.notFoundMessage, cursor.current)
	}
	
	fun match(): Boolean {
		return supportedTypes.any { cursor.match(it) }
	}
}