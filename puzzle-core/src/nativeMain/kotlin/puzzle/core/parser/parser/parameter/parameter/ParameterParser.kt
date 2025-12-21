package puzzle.core.parser.parser.parameter.parameter

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.parameter.Parameter
import puzzle.core.parser.parser.check
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseExpressionChain
import puzzle.core.parser.parser.expression.parseIdentifier
import puzzle.core.parser.parser.parseAnnotationCalls
import puzzle.core.parser.parser.parseModifiers
import puzzle.core.parser.parser.parseTypeReference
import puzzle.core.token.kinds.AssignmentKind.ASSIGN
import puzzle.core.token.kinds.BracketKind.End.RPAREN
import puzzle.core.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.token.kinds.SeparatorKind.COMMA
import puzzle.core.token.kinds.SymbolKind.COLON

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseParameters(target: ParameterTarget): List<Parameter> {
	if (!cursor.match(LPAREN)) {
		if (target.allowWithoutParen) return emptyList()
		syntaxError("${target.modifierTarget.displayName} 缺少 '('", cursor.current)
	}
	return buildList {
		while (!cursor.match(RPAREN)) {
			this += parseParameter(target)
			if (!cursor.check(RPAREN)) {
				cursor.expect(COMMA, "型参缺少 ','")
			}
		}
	}
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseParameter(target: ParameterTarget): Parameter {
	val start = cursor.current.location
	val annotationCalls = parseAnnotationCalls()
	val modifiers = parseModifiers()
	modifiers.check(target.modifierTarget)
	val name = when {
		target.allowUnnamed && cursor.offsetOrNull(offset = 1)?.kind != COLON -> null
		else -> parseIdentifier(IdentifierTarget.PARAMETER).also {
			cursor.expect(COLON, "型参缺少 ':'")
		}
	}
	val type = parseTypeReference(allowLambdaType = target.allowLambdaType)
	val defaultExpression = if (cursor.match(ASSIGN)) parseExpressionChain() else null
	val end = cursor.previous.location
	return Parameter(
		name = name,
		modifiers = modifiers,
		type = type,
		annotationCalls = annotationCalls,
		defaultExpression = defaultExpression,
		location = start span end
	)
}