package puzzle.core.parser.parser.binding.context

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider

context(_: PzlContext)
fun parseContextSpec(cursor: PzlTokenCursor): ContextSpec? {
    if (!cursor.match(PzlTokenType.CONTEXT)) return null
    return ContextSpecParser.of(cursor).parse()
}

private class ContextSpecParser private constructor(
    private val cursor: PzlTokenCursor
) : PzlParser {

    companion object : PzlParserProvider<ContextSpecParser>(::ContextSpecParser)

    context(_: PzlContext)
    fun parse(): ContextSpec {
        cursor.expect(PzlTokenType.LPAREN, "上下文缺少 '('")
        val receivers = buildList {
            do {
                this += ContextReceiverParser.of(cursor).parse()
                if (!cursor.check(PzlTokenType.RPAREN)) {
                    cursor.expect(PzlTokenType.COMMA, "上下文参数缺少 ','")
                }
            } while (!cursor.match(PzlTokenType.RPAREN))
        }
        val isInherited = cursor.match(PzlTokenType.BANG)
        return ContextSpec(receivers, isInherited)
    }
}