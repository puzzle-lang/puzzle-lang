package puzzle.core.parser.parser

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.TypeArgument
import puzzle.core.parser.parser.identifier.IdentifierNameParser
import puzzle.core.parser.parser.identifier.IdentifierNameTarget

class TypeArgumentParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<TypeArgumentParser>(::TypeArgumentParser)
	
	context(_: PzlContext)
	fun parse(): TypeArgument {
		val name = if (cursor.offsetOrNull(offset = 1)?.type == PzlTokenType.ASSIGN) {
			IdentifierNameParser.of(cursor).parse(IdentifierNameTarget.TYPE_ARGUMENT).also {
				cursor.advance()
			}
		} else null
		val type = TypeReferenceParser.of(cursor).parse(isSupportedLambdaType = true)
		return TypeArgument(name, type)
	}
}