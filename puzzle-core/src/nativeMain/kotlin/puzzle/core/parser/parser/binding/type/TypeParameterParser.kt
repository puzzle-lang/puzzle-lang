package puzzle.core.parser.parser.binding.type

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.TokenRange
import puzzle.core.parser.ast.binding.TypeParameter
import puzzle.core.parser.ast.binding.Variance
import puzzle.core.parser.ast.NamedType
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.parser.identifier.IdentifierNameParser
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.node.TypeReferenceParser

context(_: PzlContext)
fun parseTypeParameters(cursor: PzlTokenCursor): List<TypeParameter> {
	val parameters = mutableListOf<TypeParameter>()
	do {
		parameters += TypeParameterParser.of(cursor).parse()
		if (!cursor.check(PzlTokenType.GT)) {
			cursor.expect(PzlTokenType.COMMA, "缺少 ','")
		}
	} while (!cursor.match(PzlTokenType.GT))
	return parameters
}

class TypeParameterParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<TypeParameterParser>(::TypeParameterParser)
	
	context(_: PzlContext)
	fun parse(): TypeParameter {
		val start = cursor.position
		val variance = parseVariance()
		val name = IdentifierNameParser.of(cursor).parse(IdentifierNameTarget.TYPE_PARAMETER)
		val bounds = if (cursor.match(PzlTokenType.COLON)) {
			buildList<TypeReference> {
				do {
					val type = TypeReferenceParser.of(cursor).parse(isSupportedNullable = true)
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
			TypeReferenceParser.of(cursor).parse(isSupportedNullable = bounds.isEmpty() || bounds.first().isNullable)
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
	
	private fun parseVariance(): Variance? {
		return when {
			cursor.match(PzlTokenType.IN) -> Variance.IN
			cursor.match(PzlTokenType.OUT) -> Variance.OUT
			else -> null
		}
	}
}