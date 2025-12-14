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
fun parseLambdaParameters(): List<Parameter> {
	val parameters = mutableListOf<Parameter>()
	while (!cursor.match(BracketKind.End.RPAREN)) {
		parameters += parseLambdaParameter()
		if (!cursor.check(BracketKind.End.RPAREN)) {
			cursor.expect(SeparatorKind.COMMA, "参数缺少 ','")
		}
	}
	return parameters
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseLambdaParameter(): Parameter {
	val annotationCalls = parseAnnotationCalls()
	val modifiers = parseModifiers()
	modifiers.check(ModifierTarget.LAMBDA_PARAMETER)
	return parseParameter(
		modifiers = modifiers,
		annotationCalls = annotationCalls,
		isSupportedUnnameable = true,
		isSupportedLambdaType = true
	)
}