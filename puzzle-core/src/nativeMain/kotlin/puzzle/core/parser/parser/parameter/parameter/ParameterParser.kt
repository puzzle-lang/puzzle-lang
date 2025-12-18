package puzzle.core.parser.parser.parameter.parameter

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.Modifier
import puzzle.core.parser.ast.parameter.Parameter
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseExpressionChain
import puzzle.core.parser.parser.expression.parseIdentifierExpression
import puzzle.core.parser.parser.parseTypeReference
import puzzle.core.token.kinds.AssignmentKind
import puzzle.core.token.kinds.AssignmentKind.ASSIGN
import puzzle.core.token.kinds.SymbolKind.COLON

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseParameter(
	modifiers: List<Modifier>,
	annotationCalls: List<AnnotationCall>,
	isSupportedUnnameable: Boolean = false,
	isSupportedLambdaType: Boolean = false,
): Parameter {
	val start = when {
		annotationCalls.isNotEmpty() -> annotationCalls.first().location
		modifiers.isNotEmpty() -> modifiers.first().location
		else -> cursor.current.location
	}
	val name = when {
		isSupportedUnnameable && cursor.offsetOrNull(offset = 1)?.kind != COLON -> null
		else -> parseIdentifierExpression(IdentifierTarget.PARAMETER).also {
			cursor.expect(COLON, "参数缺少 ':'")
		}
	}
	val type = parseTypeReference(isSupportedLambdaType = isSupportedLambdaType)
	val defaultExpression = if (cursor.match(ASSIGN)) {
		parseExpressionChain()
	} else null
	val location = start span cursor.previous.location
	return Parameter(
		name = name,
		modifiers = modifiers,
		type = type,
		annotationCalls = annotationCalls,
		defaultExpression = defaultExpression,
		location = location
	)
}