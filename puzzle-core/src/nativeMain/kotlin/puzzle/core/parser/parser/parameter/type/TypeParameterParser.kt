package puzzle.core.parser.parser.parameter.type

import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.NamedType
import puzzle.core.parser.ast.TokenRange
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.ast.parameter.TypeParameter
import puzzle.core.parser.ast.parameter.Variance
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseIdentifierName
import puzzle.core.parser.parser.parseTypeReference

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseTypeParameters(): List<TypeParameter> {
    val parameters = mutableListOf<TypeParameter>()
    do {
        parameters += parseTypeParameter()
        if (!cursor.check(PzlTokenType.GT)) {
            cursor.expect(PzlTokenType.COMMA, "缺少 ','")
        }
    } while (!cursor.match(PzlTokenType.GT))
    return parameters
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseTypeParameter(): TypeParameter {
    val start = cursor.position
    val variance = parseVariance()
    val name = parseIdentifierName(IdentifierNameTarget.TYPE_PARAMETER)
    val bounds = if (cursor.match(PzlTokenType.COLON)) {
        buildList<TypeReference> {
            do {
                val type = parseTypeReference(isSupportedNullable = true)
                if (this.isNotEmpty()) {
                    val last = this.last()
                    if (last.isNullable != type.isNullable) {
                        val token = if (type.isNullable) {
                            cursor.previous
                        } else {
                            val type = type.type as NamedType
                            cursor.offset(offset = -type.segments.size * 2 - 1)
                        }
                        syntaxError("泛型上界指定多个类型时，可空需要一致", token)
                    }
                }
                this += type
            } while (cursor.match(PzlTokenType.BIT_AND))
        }
    } else emptyList()
    val defaultType = if (cursor.match(PzlTokenType.ASSIGN)) {
        parseTypeReference(isSupportedNullable = bounds.isEmpty() || bounds.first().isNullable)
    } else null
    val end = cursor.position
    return TypeParameter(
        name = name,
        variance = variance,
        bounds = bounds,
        defaultType = defaultType,
        location = TokenRange(start, end)
    )
}

context(cursor: PzlTokenCursor)
private fun parseVariance(): Variance? {
    return when {
        cursor.match(PzlTokenType.IN) -> Variance.IN
        cursor.match(PzlTokenType.OUT) -> Variance.OUT
        else -> null
    }
}