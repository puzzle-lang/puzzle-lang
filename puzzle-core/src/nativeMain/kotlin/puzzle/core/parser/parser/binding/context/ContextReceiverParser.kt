package puzzle.core.parser.parser.binding.context

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextReceiver
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseIdentifierName
import puzzle.core.parser.parser.parseTypeReference

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseContextReceiver(): ContextReceiver {
    val name = parseIdentifierName(IdentifierNameTarget.CONTEXT_RECEIVER)
    cursor.expect(PzlTokenType.COLON, "上下文参数缺少 ':'")
    val type = parseTypeReference()
    return ContextReceiver(name, type)
}