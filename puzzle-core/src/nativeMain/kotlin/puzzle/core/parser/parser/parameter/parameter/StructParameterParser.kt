package puzzle.core.parser.parser.parameter.parameter

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.parameter.Parameter
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.modifier.check
import puzzle.core.parser.parser.modifier.parseModifiers
import puzzle.core.parser.parser.parseAnnotationCalls
import puzzle.core.token.BracketKind
import puzzle.core.token.SeparatorKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseStructParameters(): List<Parameter> {
	cursor.expect(BracketKind.Start.LPAREN, "结构体缺少 '('")
	val parameters = mutableListOf<Parameter>()
	while (!cursor.match(BracketKind.End.RPAREN)) {
		parameters += parseStructParameter()
		if (!cursor.check(BracketKind.End.RPAREN)) {
			cursor.expect(SeparatorKind.COMMA, "参数缺少 ','")
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