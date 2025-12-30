package puzzle.core.parser.parser.statement

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.parameter.ParameterReference
import puzzle.core.parser.ast.statement.ForDestructurePattern
import puzzle.core.parser.ast.statement.ForStatement
import puzzle.core.parser.ast.statement.ForValuePattern
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseExpressionChain
import puzzle.core.parser.parser.expression.parseIdentifier
import puzzle.core.parser.parser.expression.toIdentifier
import puzzle.core.parser.parser.type.parseTypeReference
import puzzle.core.token.kinds.BracketKind
import puzzle.core.token.kinds.BracketKind.End.RBRACKET
import puzzle.core.token.kinds.BracketKind.End.RPAREN
import puzzle.core.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.token.kinds.OperatorKind.IN
import puzzle.core.token.kinds.SymbolKind.COLON
import puzzle.core.token.kinds.SymbolKind.HASH

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseForStatement(): ForStatement {
	val containLabel = cursor.offset(-2).kind == HASH
	val start = if (containLabel) cursor.offset(-3).location else cursor.previous.location
	val label = if (containLabel) cursor.offset(-3).toIdentifier() else null
	cursor.expect(LPAREN, "for 语句缺少 '('")
	val pattern = if (cursor.match(BracketKind.Start.LBRACKET)) {
		val start = cursor.previous.location
		val references = buildList {
			while (!cursor.match(RBRACKET)) {
				val name = parseIdentifier(IdentifierTarget.FOR_PARAMETER_REFERENCE)
				val type = if (cursor.match(COLON)) {
					parseTypeReference(allowLambda = true)
				} else null
				this += ParameterReference(name, type)
			}
		}
		val end = cursor.previous.location
		ForDestructurePattern(references, start span end)
	} else {
		val name = parseIdentifier(IdentifierTarget.FOR_PARAMETER_REFERENCE)
		val type = if (cursor.match(COLON)) parseTypeReference(allowLambda = true) else null
		val reference = ParameterReference(name, type)
		ForValuePattern(reference)
	}
	cursor.expect(IN, "for 语句缺少 in")
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