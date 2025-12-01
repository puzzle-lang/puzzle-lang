package puzzle.core.parser.parser.identifier

import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlToken
import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor

private val supportedTypes = arrayOf(
    PzlTokenType.IDENTIFIER,
    PzlTokenType.ABSTRACT,
    PzlTokenType.SEALED,
    PzlTokenType.OWNER,
    PzlTokenType.FINAL,
    PzlTokenType.CONST,
    PzlTokenType.TYPE,
    PzlTokenType.REIFIED,
    PzlTokenType.CONTEXT,
    PzlTokenType.INIT,
    PzlTokenType.DELETE,
    PzlTokenType.PACKAGE,
    PzlTokenType.IMPORT
)

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseIdentifierName(target: IdentifierNameTarget): String {
    supportedTypes.forEach { type ->
        if (cursor.match(type)) {
            val value = cursor.previous.value
            if (type == PzlTokenType.IDENTIFIER && value == "_" && !target.isSupportedAnonymity) {
                syntaxError(target.notSupportedAnonymityMessage, cursor.previous)
            }
            return cursor.previous.value
        }
    }
    syntaxError(target.notFoundMessage, cursor.current)
}

fun PzlToken.isIdentifier(): Boolean {
    return supportedTypes.any { this.type == it }
}

context(cursor: PzlTokenCursor)
fun matchIdentifierName(): Boolean {
    return supportedTypes.any { cursor.match(it) }
}