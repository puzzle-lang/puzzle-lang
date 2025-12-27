package puzzle.core.parser.parser.parameter.context

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.parameter.DeclarationContextReceiver
import puzzle.core.parser.ast.parameter.DeclarationContextSpec
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifier
import puzzle.core.parser.parser.type.parseTypeReference
import puzzle.core.token.kinds.BracketKind.End.RPAREN
import puzzle.core.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.token.kinds.ContextualKind.CONTEXT
import puzzle.core.token.kinds.OperatorKind.NOT
import puzzle.core.token.kinds.SeparatorKind.COMMA
import puzzle.core.token.kinds.SymbolKind.COLON

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseDeclarationContextSpec(): DeclarationContextSpec? {
	if (!cursor.match(CONTEXT)) return null
	val start = cursor.previous.location
	cursor.expect(LPAREN, "context 缺少 '('")
	val receivers = buildList {
		do {
			this += parseDeclarationContextReceiver()
			if (!cursor.check(RPAREN)) {
				cursor.expect(COMMA, "context 参数缺少 ','")
			}
		} while (!cursor.match(RPAREN))
	}
	val isPropagate = !cursor.match(NOT)
	val end = cursor.previous.location
	return DeclarationContextSpec(receivers, isPropagate, start span end)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseDeclarationContextReceiver(): DeclarationContextReceiver {
	val name = parseIdentifier(IdentifierTarget.CONTEXT_RECEIVER)
	cursor.expect(COLON, "context 参数缺少 ':'")
	val type = parseTypeReference()
	return DeclarationContextReceiver(name, type)
}