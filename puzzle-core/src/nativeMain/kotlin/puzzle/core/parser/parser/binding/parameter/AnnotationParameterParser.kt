package puzzle.core.parser.parser.binding.parameter

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.Parameter
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.parser.modifier.check
import puzzle.core.parser.parser.modifier.parseModifiers
import puzzle.core.symbol.Modifier

context(_: PzlContext)
fun parseAnnotationParameters(
	cursor: PzlTokenCursor
): List<Parameter> {
	if (!cursor.match(PzlTokenType.LPAREN)) {
		return emptyList()
	}
	val parameters = mutableListOf<Parameter>()
	while (!cursor.match(PzlTokenType.RPAREN)) {
		parameters += AnnotationParameterParser.of(cursor).parse()
		if (!cursor.check(PzlTokenType.RPAREN)) {
			cursor.expect(PzlTokenType.COMMA, "参数缺少 ','")
		}
	}
	return parameters
}

class AnnotationParameterParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<AnnotationParameterParser>(::AnnotationParameterParser)
	
	context(_: PzlContext)
	fun parse(): Parameter {
		val modifiers = parseModifiers(cursor)
		modifiers.check(cursor, ModifierTarget.ANNOTATION_PARAMETER)
		if (Modifier.VAL !in modifiers) {
			println(cursor.current.type)
			syntaxError("注解参数必须添加 'val' 修饰符", cursor.current)
		}
		return ParameterParser.of(cursor).parse()
	}
}