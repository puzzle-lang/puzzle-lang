package puzzle.core.parser.binding.generic.parser

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlParser
import puzzle.core.parser.PzlParserProvider
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.binding.generic.GenericDefinition

context(_: PzlContext)
fun parseGenericDefinition(cursor: PzlTokenCursor): GenericDefinition? {
	return when {
		cursor.match(PzlTokenType.REIFIED, PzlTokenType.TYPE) ->
			GenericDefinitionParser.of(cursor).parse(isReified = true)
		
		cursor.match(PzlTokenType.TYPE) -> GenericDefinitionParser.of(cursor).parse()
		else -> null
	}
}

class GenericDefinitionParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<GenericDefinitionParser>(::GenericDefinitionParser)
	
	context(_: PzlContext)
	fun parse(isReified: Boolean = false): GenericDefinition {
		cursor.expect(PzlTokenType.LT, "'generic' 后必须跟 '<'")
		val parameters = parseGenericParameters(cursor)
		return GenericDefinition(isReified, parameters)
	}
}