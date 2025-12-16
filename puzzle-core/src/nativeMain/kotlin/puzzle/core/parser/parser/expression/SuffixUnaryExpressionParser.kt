package puzzle.core.parser.parser.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.IdentifierExpression
import puzzle.core.parser.ast.expression.SuffixUnaryExpression
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseIdentifierName
import puzzle.core.token.OperatorKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseSuffixUnaryExpression(): SuffixUnaryExpression {
	val name = parseIdentifierName(IdentifierNameTarget.SUFFIX_UNARY)
	val expression = IdentifierExpression(name)
	val operator = cursor.current.kind as OperatorKind
	cursor.advance()
	return SuffixUnaryExpression(expression, operator)
}