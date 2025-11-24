package puzzle.core.parser.parser.binding

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.GenericSpec

context(_: PzlContext)
fun parseGenericSpec(cursor: PzlTokenCursor): GenericSpec? {
	return when {
		cursor.match(PzlTokenType.REIFIED, PzlTokenType.TYPE) ->
			GenericSpecParser.of(cursor).parse(isReified = true)
		
		cursor.match(PzlTokenType.TYPE) -> GenericSpecParser.of(cursor).parse()
		else -> null
	}
}

class GenericSpecParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object Companion : PzlParserProvider<GenericSpecParser>(::GenericSpecParser)
	
	context(_: PzlContext)
	fun parse(isReified: Boolean = false): GenericSpec {
		cursor.expect(PzlTokenType.LT, "'generic' 后必须跟 '<'")
		val parameters = parseGenericParameters(cursor)
		return GenericSpec(isReified, parameters)
	}
}