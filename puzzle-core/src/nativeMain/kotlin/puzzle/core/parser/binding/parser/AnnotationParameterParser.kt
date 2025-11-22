package puzzle.core.parser.binding.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.binding.Parameter
import puzzle.core.parser.binding.parseParameter
import puzzle.core.parser.checkModifiers
import puzzle.core.parser.declaration.NodeKind
import puzzle.core.parser.parseModifiers

context(_: PzlContext)
fun parseAnnotationParameters(
	cursor: PzlTokenCursor
): List<Parameter> {
	if (!cursor.match(PzlTokenType.LPAREN)) {
		return emptyList()
	}
	val parameters = mutableListOf<Parameter>()
	while (!cursor.match(PzlTokenType.RPAREN)) {
		parameters += AnnotationParameterParser(cursor).parse()
		if (!cursor.check(PzlTokenType.RPAREN)) {
			cursor.expect(PzlTokenType.COMMA, "参数缺少 ','")
		}
	}
	return parameters
}

class AnnotationParameterParser(
	private val cursor: PzlTokenCursor
) {
	
	context(_: PzlContext)
	fun parse(): Parameter {
		val modifiers = parseModifiers(cursor)
		checkModifiers(cursor, modifiers, NodeKind.ANNOTATION_PARAMETER)
		if (Modifier.VAL !in modifiers) {
			println(cursor.current.type)
			syntaxError("注解参数必须添加 'val' 修饰符", cursor.current)
		}
		return parseParameter(cursor, isSupportedLambdaType = false)
	}
}