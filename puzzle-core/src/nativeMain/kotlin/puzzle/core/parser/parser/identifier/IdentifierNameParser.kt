package puzzle.core.parser.parser.identifier

import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlToken
import puzzle.core.lexer.PzlTokenType.*
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.NamedType
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.parser.parseTypeReference

private val softKeywords = setOf(
    PRIVATE, PROTECTED, FILE, INTERNAL, MODULE, PUBLIC,
    FINAL,
    OPEN, ABSTRACT, SEALED, OVERRIDE,
    CONST, OWNER, IGNORE, LATE, LAZY, ARGS,
    GET, SET,
    TYPE, REIFIED,
    CONTEXT,
    INIT, DELETE,
    PACKAGE, IMPORT
)

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseIdentifierName(target: IdentifierNameTarget): String {
    if (cursor.match(IDENTIFIER)) {
        val value = cursor.previous.value
        if (value == "_" && !target.isSupportedAnonymity) {
            syntaxError(target.notSupportedAnonymityMessage, cursor.previous)
        }
        return value
    }
    softKeywords.forEach { type ->
        if (cursor.match(type)) {
            return cursor.previous.value
        }
    }
    syntaxError(target.notFoundMessage, cursor.current)
}

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseExtensionAndIdentifierName(target: IdentifierNameTarget): Pair<TypeReference?, String> {
    val name = parseIdentifierName(target)
    return if (cursor.check(DOT)) {
        cursor.retreat()
        val type = parseTypeReference()
        if (type.isNullable) {
            cursor.expect(DOT, "${target.name}缺少 '.'")
            val name = parseIdentifierName(IdentifierNameTarget.FUN)
            type to name
        } else {
            val segments = (type.type as NamedType).segments.toMutableList()
            val name = segments.removeLast()
            val type = TypeReference(NamedType(segments))
            type to name
        }
    } else {
        null to name
    }
}

fun PzlToken.isIdentifier(): Boolean {
    return this.type == IDENTIFIER || softKeywords.any { this.type == it }
}

context(cursor: PzlTokenCursor)
fun matchIdentifierName(): Boolean {
    return cursor.match(IDENTIFIER) || softKeywords.any { cursor.match(it) }
}