package puzzle.core.parser.binding.generic.parser

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlParser
import puzzle.core.parser.PzlParserProvider
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.binding.generic.GenericParameter
import puzzle.core.parser.binding.generic.Variance
import puzzle.core.parser.node.parser.TypeReferenceParser

context(_: PzlContext)
fun parseGenericParameters(cursor: PzlTokenCursor): List<GenericParameter> {
	val parameters = mutableListOf<GenericParameter>()
	do {
		parameters += GenericParameterParser.of(cursor).parse()
		if (!cursor.check(PzlTokenType.GT)) {
			cursor.expect(PzlTokenType.COMMA, "缺少 ','")
		}
	} while (!cursor.match(PzlTokenType.GT))
	return parameters
}

class GenericParameterParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object Companion : PzlParserProvider<GenericParameterParser>(::GenericParameterParser)
	
	context(_: PzlContext)
	fun parse(): GenericParameter {
		val variance = parseVariance()
		cursor.expect(PzlTokenType.IDENTIFIER, "缺少泛型名称")
		val name = cursor.previous.value
		val strictBound = when {
			cursor.match(PzlTokenType.COLON) -> false
			cursor.match(PzlTokenType.STRICT_BOUND) -> true
			else -> return GenericParameter(
				name = name,
				variance = variance,
			)
		}
		val bound = TypeReferenceParser.of(cursor).parse()
		if (!cursor.match(PzlTokenType.ASSIGN)) {
			return GenericParameter(
				name = name,
				variance = variance,
				strictBound = strictBound,
				bound = bound
			)
		}
		val defaultType = TypeReferenceParser.of(cursor).parse(isSupportedNullable = bound.isNullable)
		return GenericParameter(
			name = name,
			variance = variance,
			strictBound = strictBound,
			bound = bound,
			defaultType = defaultType
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