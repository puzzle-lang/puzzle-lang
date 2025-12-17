package puzzle.core.parser.parser

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.parser.expression.parseArguments
import puzzle.core.token.kinds.BracketKind
import puzzle.core.token.kinds.SymbolKind
import puzzle.core.model.span

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseAnnotationCalls(): List<AnnotationCall> {
	if (!cursor.match(SymbolKind.AT)) {
		return emptyList()
	}
	return buildList {
		do {
			this += parseAnnotationCall()
		} while (cursor.match(SymbolKind.AT))
	}
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseAnnotationCall(): AnnotationCall {
	val start = cursor.previous.location
	val type = parseTypeReference(isSupportedLambdaType = false)
	if (!cursor.match(BracketKind.Start.LPAREN)) {
		val location = start span cursor.previous.location
		return AnnotationCall(type, location)
	}
	val arguments = parseArguments(BracketKind.End.RPAREN)
	val location = start span cursor.previous.location
	return AnnotationCall(type, location, arguments)
}