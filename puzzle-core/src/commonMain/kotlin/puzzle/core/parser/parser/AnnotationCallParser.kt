package puzzle.core.parser.parser

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.parser.expression.parseArguments
import puzzle.core.token.kinds.BracketKind.End.RPAREN
import puzzle.core.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.token.kinds.SymbolKind.AT

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseAnnotationCalls(): List<AnnotationCall> {
	if (!cursor.match(AT)) {
		return emptyList()
	}
	return buildList {
		do {
			this += parseAnnotationCall()
		} while (cursor.match(AT))
	}
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseAnnotationCall(): AnnotationCall {
	val start = cursor.previous.location
	val type = parseTypeReference(allowLambdaType = false)
	if (!cursor.match(LPAREN)) {
		val location = start span cursor.previous.location
		return AnnotationCall(type, location)
	}
	val arguments = parseArguments(RPAREN)
	val location = start span cursor.previous.location
	return AnnotationCall(type, location, arguments)
}