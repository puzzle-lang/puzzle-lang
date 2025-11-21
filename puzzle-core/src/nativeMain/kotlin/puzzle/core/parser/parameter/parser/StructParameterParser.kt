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
fun parseStructParameters(cursor: PzlTokenCursor, structAccess: Modifier): List<Parameter> {
	cursor.expect(PzlTokenType.LPAREN, "结构体缺少 '('")
	val parameters = mutableListOf<Parameter>()
	while (!cursor.match(PzlTokenType.RPAREN)) {
		parameters += StructParameterParser(cursor).parse(structAccess)
		if (!cursor.check(PzlTokenType.RPAREN)) {
			cursor.expect(PzlTokenType.COMMA, "参数缺少 ','")
		}
	}
	return parameters
}

private class StructParameterParser(
	private val cursor: PzlTokenCursor
) {
	
	context(_: PzlContext)
	fun parse(structAccess: Modifier): Parameter {
		val modifiers = mutableSetOf<Modifier>()
		modifiers += getDefaultMemberAccessModifier(structAccess)
		if (cursor.match(PzlTokenType.IGNORE)) {
			modifiers += Modifier.IGNORE
		}
		modifiers += when {
			cursor.match(PzlTokenType.VAR) -> Modifier.VAR
			cursor.match(PzlTokenType.VAL) -> Modifier.VAL
			else -> syntaxError("结构体参数必须添加 'var' 或 'val' 修饰符", cursor.current)
		}
		return parseParameter(cursor, modifiers, isSupportedLambdaType = false)
	}
}