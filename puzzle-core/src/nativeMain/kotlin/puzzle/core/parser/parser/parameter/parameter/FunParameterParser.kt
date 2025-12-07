package puzzle.core.parser.parser.parameter.parameter

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.parameter.Parameter
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.modifier.check
import puzzle.core.parser.parser.modifier.parseModifiers

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseFunParameters(): List<Parameter> {
	cursor.expect(PzlTokenType.LPAREN, "函数缺少 '('")
	if (cursor.match(PzlTokenType.RPAREN)) {
		return emptyList()
	}
	val parameters = mutableListOf<Parameter>()
	while (!cursor.match(PzlTokenType.RPAREN)) {
		parameters += parseFunParameter()
		if (!cursor.check(PzlTokenType.RPAREN)) {
			cursor.expect(PzlTokenType.COMMA, "参数缺少 ','")
		}
	}
	return parameters
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseFunParameter(): Parameter {
	val modifiers = parseModifiers()
	modifiers.check(ModifierTarget.FUN_PARAMETER)
	return parseParameter(modifiers, isSupportedLambdaType = true)
}