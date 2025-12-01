package puzzle.core.parser.parser.binding.context

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseContextSpec(): ContextSpec? {
    if (!cursor.match(PzlTokenType.CONTEXT)) return null
    cursor.expect(PzlTokenType.LPAREN, "上下文缺少 '('")
    val receivers = buildList {
        do {
            this += parseContextReceiver()
            if (!cursor.check(PzlTokenType.RPAREN)) {
                cursor.expect(PzlTokenType.COMMA, "上下文参数缺少 ','")
            }
        } while (!cursor.match(PzlTokenType.RPAREN))
    }
    val isInherited = cursor.match(PzlTokenType.BANG)
    return ContextSpec(receivers, isInherited)
}