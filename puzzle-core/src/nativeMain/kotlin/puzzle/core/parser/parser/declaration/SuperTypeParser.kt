package puzzle.core.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.NamedType
import puzzle.core.parser.ast.declaration.SuperClass
import puzzle.core.parser.ast.declaration.SuperTrait
import puzzle.core.parser.ast.declaration.SuperType
import puzzle.core.parser.ast.expression.InvokeType
import puzzle.core.parser.parser.expression.parseArguments
import puzzle.core.parser.parser.parseTypeReference

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseSuperTypes(
    isSupportedClass: Boolean = true
): List<SuperType> {
    if (!cursor.match(PzlTokenType.COLON)) {
        return emptyList()
    }
    val superTypes = mutableListOf<SuperType>()
    do {
        superTypes += parseSuperType(
            isSupportedClass = isSupportedClass,
            hasSuperClass = superTypes.any { it is SuperClass }
        )
    } while (cursor.match(PzlTokenType.COMMA))
    return superTypes
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseSuperType(
    isSupportedClass: Boolean,
    hasSuperClass: Boolean
): SuperType {
    val type = parseTypeReference(isSupportedNullable = false)
    if (!cursor.match(PzlTokenType.LPAREN)) {
        return SuperTrait(type)
    }
    val offset = -1 - ((type.type as NamedType).segments.size - 1) * 2
    if (!isSupportedClass) {
        syntaxError("不支持继承类", cursor.offset(offset))
    }
    if (hasSuperClass) {
        syntaxError("不支持继承多个类", cursor.offset(offset))
    }
    val arguments = parseArguments(InvokeType.CALL)
    return SuperClass(type, arguments)
}