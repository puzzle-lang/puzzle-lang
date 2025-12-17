package puzzle.core.parser.parser.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.Operator
import puzzle.core.parser.ast.expression.SuffixUnaryExpression
import puzzle.core.token.kinds.OperatorKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseSuffixUnaryExpression(): SuffixUnaryExpression {
	val name = parseIdentifierExpression(IdentifierTarget.SUFFIX_UNARY)
	val token = cursor.current
	val operator = Operator(token.kind as OperatorKind, token.location)
	cursor.advance()
	return SuffixUnaryExpression(name, operator)
}