package puzzle.core.parser.parser.parameter.parameter

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.parameter.Parameter
import puzzle.core.parser.parser.expression.parseExpressionChain
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseIdentifierName
import puzzle.core.parser.parser.parseTypeReference
import puzzle.core.symbol.Modifier

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseParameter(
	modifiers: List<Modifier>,
	annotationCalls: List<AnnotationCall>,
	isSupportedUnnameable: Boolean = false,
	isSupportedLambdaType: Boolean = false
): Parameter {
	val name = when {
		isSupportedUnnameable && cursor.offsetOrNull(offset = 1)?.type != PzlTokenType.COLON -> null
		else -> parseIdentifierName(IdentifierNameTarget.PARAMETER).also {
			cursor.expect(PzlTokenType.COLON, "参数缺少 ':'")
		}
	}
	val type = parseTypeReference(isSupportedLambdaType = isSupportedLambdaType)
	val defaultExpression = if (cursor.match(PzlTokenType.ASSIGN)) {
		parseExpressionChain()
	} else null
	return Parameter(
		name = name,
		modifiers = modifiers,
		type = type,
		annotationCalls = annotationCalls,
		defaultExpression = defaultExpression
	)
}