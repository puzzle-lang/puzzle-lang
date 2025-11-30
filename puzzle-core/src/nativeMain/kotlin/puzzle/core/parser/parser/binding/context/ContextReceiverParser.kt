package puzzle.core.parser.parser.binding.context

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextReceiver
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.parser.identifier.IdentifierNameParser
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.TypeReferenceParser

class ContextReceiverParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<ContextReceiverParser>(::ContextReceiverParser)
	
	context(_: PzlContext)
	fun parse(): ContextReceiver {
		val name = IdentifierNameParser.of(cursor).parse(IdentifierNameTarget.CONTEXT_RECEIVER)
		cursor.expect(PzlTokenType.COLON, "上下文参数缺少 ':'")
		val type = TypeReferenceParser.of(cursor).parse()
		return ContextReceiver(name, type)
	}
}