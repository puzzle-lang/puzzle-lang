package puzzle.core.parser.parser.parameter.parameter

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.parameter.Parameter
import puzzle.core.parser.parser.ModifierTarget
import puzzle.core.parser.parser.check
import puzzle.core.parser.parser.parseAnnotationCalls
import puzzle.core.parser.parser.parseModifiers
import puzzle.core.token.kinds.BracketKind.End.RPAREN
import puzzle.core.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.token.kinds.SeparatorKind.COMMA

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseStructParameters(): List<Parameter> {
	cursor.expect(LPAREN, "结构体缺少 '('")
	val parameters = mutableListOf<Parameter>()
	while (!cursor.match(RPAREN)) {
		parameters += parseStructParameter()
		if (!cursor.check(RPAREN)) {
			cursor.expect(COMMA, "参数缺少 ','")
		}
	}
	return parameters
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseStructParameter(): Parameter {
	val annotationCalls = parseAnnotationCalls()
	val modifiers = parseModifiers()
	modifiers.check(ModifierTarget.STRUCT_PARAMETER)
	return parseParameter(modifiers, annotationCalls)
}