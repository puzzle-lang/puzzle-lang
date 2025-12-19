package puzzle.core.parser.parser.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.Operator
import puzzle.core.parser.ast.expression.Identifier
import puzzle.core.parser.ast.expression.PrefixUnaryExpression
import puzzle.core.token.kinds.OperatorKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parsePrefixUnaryExpression(): PrefixUnaryExpression {
	val token = cursor.previous
	val operator = Operator(token.kind as OperatorKind, token.location)
	val name = parseIdentifier(IdentifierTarget.PREFIX_UNARY)
	val expression = Identifier(name.name, name.location)
	return PrefixUnaryExpression(operator, expression)
}