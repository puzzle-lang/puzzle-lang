package puzzle.core.parser.parameter.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.getClassParameterAccessModifier
import puzzle.core.parser.getDefaultMemberAccessModifier
import puzzle.core.parser.parameter.Parameter
import puzzle.core.parser.parameter.parseParameter

context(_: PzlContext)
fun parseClassParameters(
	cursor: PzlTokenCursor,
	classAccess: Modifier
): List<Parameter> {
	if (!cursor.match(PzlTokenType.LPAREN)) {
		return emptyList()
	}
	val parameters = mutableListOf<Parameter>()
	while (!cursor.match(PzlTokenType.RPAREN)) {
		parameters += ClassParameterParser(cursor).parse(classAccess)
		if (!cursor.check(PzlTokenType.RPAREN)) {
			cursor.expect(PzlTokenType.COMMA, "参数缺少 ','")
		}
	}
	return parameters
}

private class ClassParameterParser(
	private val cursor: PzlTokenCursor
) {
	
	context(_: PzlContext)
	fun parse(classAccess: Modifier): Parameter {
		val modifiers = mutableSetOf<Modifier>()
		var parameterAccess = getClassParameterAccessModifier(cursor, classAccess) {
			"主构造参数访问修饰符与类访问修饰符不兼容"
		}
		val visibleModifier = when {
			cursor.match(PzlTokenType.VAR) -> Modifier.VAR
			cursor.match(PzlTokenType.VAL) -> Modifier.VAL
			else -> if (parameterAccess != null) {
				syntaxError("主构造参数使用访问修饰符后必须跟 'var' 或 'val' 修饰符", cursor.current)
			} else null
		}
		if (visibleModifier != null && parameterAccess == null) {
			parameterAccess = getDefaultMemberAccessModifier(classAccess)
		}
		if (parameterAccess != null) {
			modifiers += parameterAccess
		}
		if (visibleModifier != null) {
			modifiers += visibleModifier
		}
		return parseParameter(cursor, modifiers)
	}
}