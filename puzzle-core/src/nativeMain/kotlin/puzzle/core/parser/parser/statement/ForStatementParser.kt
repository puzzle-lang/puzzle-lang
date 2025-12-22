package puzzle.core.parser.parser.statement

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.ForStatement
import puzzle.core.parser.ast.statement.IndexValuePattern
import puzzle.core.parser.ast.statement.ValuePattern
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseExpressionChain
import puzzle.core.parser.parser.expression.parseIdentifier
import puzzle.core.parser.parser.expression.toIdentifier
import puzzle.core.token.kinds.BracketKind.End.RPAREN
import puzzle.core.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.token.kinds.SeparatorKind.COMMA
import puzzle.core.token.kinds.SymbolKind.COLON
import puzzle.core.token.kinds.SymbolKind.HASH

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseForStatement(): ForStatement {
	val containLabel = cursor.offset(-2).kind == HASH
	val start = if (containLabel) cursor.offset(-3).location else cursor.previous.location
	val label = if (containLabel) cursor.offset(-3).toIdentifier() else null
	cursor.expect(LPAREN, "for 语句缺少 '{'")
	val value = parseIdentifier(IdentifierTarget.FOR_VARIABLE)
	val pattern = if (cursor.match(COMMA)) {
		val index = value
		val value = parseIdentifier(IdentifierTarget.FOR_VARIABLE)
		IndexValuePattern(index, value)
	} else {
		ValuePattern(value)
	}
	cursor.expect(COLON, "for 语句缺少 ':'")
	val iterable = parseExpressionChain()
	cursor.expect(RPAREN, "for 语句缺少 ')'")
	val body = if (cursor.match(LBRACE)) {
		parseStatements()
	} else {
		listOf(parseStatement())
	}
	val end = cursor.previous.location
	return ForStatement(label, pattern, iterable, body, start span end)
}