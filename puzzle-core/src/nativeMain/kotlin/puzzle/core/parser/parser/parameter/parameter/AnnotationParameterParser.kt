package puzzle.core.parser.parser.parameter.parameter

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.parameter.Parameter
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.modifier.check
import puzzle.core.parser.parser.modifier.parseModifiers
import puzzle.core.parser.parser.parseAnnotationCalls
import puzzle.core.token.kinds.BracketKind.End.RPAREN
import puzzle.core.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.token.kinds.ModifierKind.VAL
import puzzle.core.token.kinds.SeparatorKind.COMMA

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseAnnotationParameters(): List<Parameter> {
	if (!cursor.match(LPAREN)) {
		return emptyList()
	}
	val parameters = mutableListOf<Parameter>()
	while (!cursor.match(RPAREN)) {
		parameters += parseAnnotationParameter()
		if (!cursor.check(RPAREN)) {
			cursor.expect(COMMA, "参数缺少 ','")
		}
	}
	return parameters
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseAnnotationParameter(): Parameter {
	val annotationCalls = parseAnnotationCalls()
	val modifiers = parseModifiers()
	modifiers.check(ModifierTarget.ANNOTATION_PARAMETER)
	if (modifiers.all { it.kind != VAL }) {
		syntaxError("注解参数必须添加 'val' 修饰符", cursor.current)
	}
	return parseParameter(modifiers, annotationCalls)
}