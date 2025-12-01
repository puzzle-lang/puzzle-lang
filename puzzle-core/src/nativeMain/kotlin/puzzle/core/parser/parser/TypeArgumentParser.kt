package puzzle.core.parser.parser

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.TypeArgument
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseIdentifierName

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseTypeArguments(): List<TypeArgument> {
    if (!cursor.match(PzlTokenType.LT)) {
        return emptyList()
    }
    val arguments = mutableListOf<TypeArgument>()
    do {
        arguments += parseTypeArgument()
        if (!cursor.check(PzlTokenType.GT)) {
            cursor.expect(PzlTokenType.COMMA, "泛型参数缺少 ','")
        }
    } while (!cursor.match(PzlTokenType.GT))
    return arguments
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseTypeArgument(): TypeArgument {
    val name = if (cursor.offsetOrNull(offset = 1)?.type == PzlTokenType.ASSIGN) {
        parseIdentifierName(IdentifierNameTarget.TYPE_ARGUMENT).also {
            cursor.advance()
        }
    } else null
    val type = parseTypeReference(isSupportedLambdaType = true)
    return TypeArgument(name, type)
}