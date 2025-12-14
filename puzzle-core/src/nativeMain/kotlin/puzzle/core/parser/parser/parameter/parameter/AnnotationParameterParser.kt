package puzzle.core.parser.parser.parameter.parameter

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.parameter.Parameter
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.modifier.check
import puzzle.core.parser.parser.modifier.parseModifiers
import puzzle.core.parser.parser.parseAnnotationCalls
import puzzle.core.token.BracketKind
import puzzle.core.token.ModifierKind
import puzzle.core.token.SeparatorKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseAnnotationParameters(): List<Parameter> {
	if (!cursor.match(BracketKind.Start.LPAREN)) {
		return emptyList()
	}
	val parameters = mutableListOf<Parameter>()
	while (!cursor.match(BracketKind.End.RPAREN)) {
		parameters += parseAnnotationParameter()
		if (!cursor.check(BracketKind.End.RPAREN)) {
			cursor.expect(SeparatorKind.COMMA, "参数缺少 ','")
		}
	}
	return parameters
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseAnnotationParameter(): Parameter {
	val annotationCalls = parseAnnotationCalls()
	val modifiers = parseModifiers()
	modifiers.check(ModifierTarget.ANNOTATION_PARAMETER)
	if (ModifierKind.VAL !in modifiers) {
		syntaxError("注解参数必须添加 'val' 修饰符", cursor.current)
	}
	return parseParameter(modifiers, annotationCalls)
}