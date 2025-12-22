package puzzle.core.parser.parser.expression

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.LoopExpression
import puzzle.core.parser.parser.statement.parseStatement
import puzzle.core.parser.parser.statement.parseStatements
import puzzle.core.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.token.kinds.SymbolKind.HASH

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseLoopExpression(): LoopExpression {
	val containLabel = cursor.offset(-2).kind == HASH
	val start = if (containLabel) cursor.offset(-3).location else cursor.previous.location
	val label = if (containLabel) cursor.offset(-3).toIdentifier() else null
	val body = if (cursor.match(LBRACE)) {
		parseStatements()
	} else {
		listOf(parseStatement())
	}
	val end = cursor.previous.location
	return LoopExpression(label, body, start span end)
}