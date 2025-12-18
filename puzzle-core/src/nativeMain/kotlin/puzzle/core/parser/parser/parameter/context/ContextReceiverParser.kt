package puzzle.core.parser.parser.parameter.context

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.parameter.ContextReceiver
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifierExpression
import puzzle.core.parser.parser.parseTypeReference
import puzzle.core.token.kinds.SymbolKind.COLON

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseContextReceiver(): ContextReceiver {
	val name = parseIdentifierExpression(IdentifierTarget.CONTEXT_RECEIVER)
	cursor.expect(COLON, "上下文参数缺少 ':'")
	val type = parseTypeReference()
	return ContextReceiver(name, type)
}