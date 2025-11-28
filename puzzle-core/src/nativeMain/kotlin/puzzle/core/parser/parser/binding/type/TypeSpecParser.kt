package puzzle.core.parser.parser.binding.type

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.TokenRange
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.binding.Variance
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider

context(_: PzlContext)
fun parseTypeSpec(cursor: PzlTokenCursor): TypeSpec? {
    return when {
        cursor.match(PzlTokenType.REIFIED, PzlTokenType.TYPE) ->
            TypeSpecParser.of(cursor).parse(isReified = true)

        cursor.match(PzlTokenType.TYPE) -> TypeSpecParser.of(cursor).parse()
        else -> null
    }
}

context(_: PzlContext)
fun TypeSpec.check(
    cursor: PzlTokenCursor,
    target: TypeTarget
) {
    if (!target.isSupportedType) {
        syntaxError("${target.displayName}不支持泛型", cursor[this.location.start])
    }
    if (!target.isSupportedVariance) {
        this.parameters.forEach {
            if (it.variance != null) {
                val variance = when (it.variance) {
                    Variance.IN -> "in"
                    Variance.OUT -> "out"
                }
                syntaxError("${target.displayName}不支持使用 '$variance'", cursor[it.location.start])
            }
        }
    }
}

private class TypeSpecParser private constructor(
    private val cursor: PzlTokenCursor
) : PzlParser {

    companion object : PzlParserProvider<TypeSpecParser>(::TypeSpecParser)

    context(_: PzlContext)
    fun parse(isReified: Boolean = false): TypeSpec {
        val start = if (isReified) cursor.position - 2 else cursor.position - 1
        cursor.expect(PzlTokenType.LT, "'type' 后必须跟 '<'")
        val parameters = parseTypeParameters(cursor)
        val end = cursor.position
        return TypeSpec(
            isReified = isReified,
            parameters = parameters,
            location = TokenRange(start, end),
        )
    }
}