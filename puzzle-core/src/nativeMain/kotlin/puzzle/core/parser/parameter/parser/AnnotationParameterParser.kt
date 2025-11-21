package puzzle.core.parser.parameter.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.getDefaultMemberAccessModifier
import puzzle.core.parser.parameter.Parameter
import puzzle.core.parser.parameter.parseParameter

context(_: PzlContext)
fun parseAnnotationParameters(
	cursor: PzlTokenCursor,
	annotationAccess: Modifier
): List<Parameter> {
	if (!cursor.match(PzlTokenType.LPAREN)) {
		return emptyList()
	}
	val parameters = mutableListOf<Parameter>()
	while (!cursor.match(PzlTokenType.RPAREN)) {
		parameters += AnnotationParameterParser(cursor).parse(annotationAccess)
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
	fun parse(annotationAccess: Modifier): Parameter {
		val modifiers = mutableSetOf<Modifier>()
		modifiers += getDefaultMemberAccessModifier(annotationAccess)
		modifiers += when {
			cursor.match(PzlTokenType.VAR) -> syntaxError("注解参数不支持 'var' 修饰符", cursor.current)
			cursor.match(PzlTokenType.VAL) -> Modifier.VAL
			else -> syntaxError("注解参数必须添加 'val' 修饰符", cursor.current)
		}
		return parseParameter(cursor, modifiers, isSupportedLambdaType = false)
	}
}