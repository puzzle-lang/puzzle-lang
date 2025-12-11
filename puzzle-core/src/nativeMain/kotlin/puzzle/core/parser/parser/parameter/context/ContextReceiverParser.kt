package puzzle.core.parser.parser.parameter.context

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.parameter.ContextReceiver
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseIdentifierName
import puzzle.core.parser.parser.parseTypeReference
import puzzle.core.token.SymbolKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseContextReceiver(): ContextReceiver {
	val name = parseIdentifierName(IdentifierNameTarget.CONTEXT_RECEIVER)
	cursor.expect(SymbolKind.COLON, "上下文参数缺少 ':'")
	val type = parseTypeReference()
	return ContextReceiver(name, type)
}