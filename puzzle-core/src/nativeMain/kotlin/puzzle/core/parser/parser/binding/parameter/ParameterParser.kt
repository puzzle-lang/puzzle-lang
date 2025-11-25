package puzzle.core.parser.parser.binding.parameter

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.Parameter
import puzzle.core.parser.matcher.expression.parseCompleteExpression
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.parser.VariableNameParser
import puzzle.core.parser.parser.node.TypeReferenceParser
import puzzle.core.symbol.Modifier

class ParameterParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<ParameterParser>(::ParameterParser)
	
	context(_: PzlContext)
	fun parse(
		modifiers: List<Modifier> = emptyList(),
		isSupportedTypeOnly: Boolean = false,
		isSupportedLambdaType: Boolean = false
	): Parameter {
		val name = when {
			isSupportedTypeOnly && cursor.offsetOrNull(offset = 1)?.type != PzlTokenType.COLON -> null
			else -> {
				val name = VariableNameParser.of(cursor).parse("参数")
				cursor.expect(PzlTokenType.COLON, "参数缺少 ':'")
				name
			}
		}
		val typeReference = TypeReferenceParser.of(cursor).parse(isSupportedLambdaType)
		val defaultExpression = if (cursor.match(PzlTokenType.ASSIGN)) {
			parseCompleteExpression(cursor)
		} else null
		return Parameter(
			name = name,
			modifiers = modifiers,
			typeReference = typeReference,
			defaultExpression = defaultExpression
		)
	}
}