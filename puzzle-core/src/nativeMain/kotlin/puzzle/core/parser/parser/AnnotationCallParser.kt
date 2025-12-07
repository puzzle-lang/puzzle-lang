package puzzle.core.parser.parser

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.expression.InvokeType
import puzzle.core.parser.parser.expression.parseArguments

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseAnnotationCalls(): List<AnnotationCall> {
	if (!cursor.match(PzlTokenType.AT)) {
		return emptyList()
	}
	return buildList {
		do {
			this += parseAnnotationCall()
		} while (cursor.match(PzlTokenType.AT))
	}
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseAnnotationCall(): AnnotationCall {
	val type = parseTypeReference(isSupportedLambdaType = false)
	if (!cursor.match(PzlTokenType.LPAREN)) {
		return AnnotationCall(type)
	}
	val arguments = parseArguments(InvokeType.CALL)
	return AnnotationCall(type, arguments)
}