package puzzle.core.parser.parser.parameter.context

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.parameter.LambdaContextReceiver
import puzzle.core.parser.ast.parameter.LambdaContextSpec
import puzzle.core.parser.parser.expression.checkIdentifier
import puzzle.core.parser.parser.parameter.parseTypeExpansion
import puzzle.core.parser.parser.type.parseTypeReference
import puzzle.core.token.kinds.BracketKind.End.RPAREN
import puzzle.core.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.token.kinds.ContextualKind.CONTEXT
import puzzle.core.token.kinds.OperatorKind.NOT
import puzzle.core.token.kinds.SeparatorKind.COMMA
import puzzle.core.token.kinds.SymbolKind.COLON

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseLambdaContextSpec(): LambdaContextSpec? {
	if (!cursor.check(CONTEXT) || cursor.nextOrNull?.kind != LPAREN) return null
	val start = cursor.current.location
	cursor.advance(2)
	val receivers = buildList {
		do {
			if (cursor.checkIdentifier(allowAnonymousBinding = true) && cursor.nextOrNull?.kind == COLON) {
				syntaxError("lambda 上下文参数不支持命名", cursor.current)
			}
			this += parseLambdaContextReceiver()
			if (!cursor.check(RPAREN)) {
				cursor.expect(COMMA, "context 参数列表缺少 ','")
			}
		} while (!cursor.match(RPAREN))
	}
	val isPropagate = !cursor.match(NOT)
	val end = cursor.previous.location
	return LambdaContextSpec(receivers, isPropagate, start span end)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseLambdaContextReceiver(): LambdaContextReceiver {
	val type = parseTypeReference(allowLambda = true)
	val typeExpansion = parseTypeExpansion()
	val end = cursor.previous.location
	return LambdaContextReceiver(type, typeExpansion, type.location span end)
}