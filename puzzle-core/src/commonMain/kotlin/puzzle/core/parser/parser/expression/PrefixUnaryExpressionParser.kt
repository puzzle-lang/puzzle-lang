package puzzle.core.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.Operator
import puzzle.core.parser.ast.expression.Identifier
import puzzle.core.parser.ast.expression.MemberAccessExpression
import puzzle.core.parser.ast.expression.PrefixUnaryExpression
import puzzle.core.parser.ast.expression.SuffixUnaryExpression
import puzzle.core.token.kinds.OperatorKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parsePrefixUnaryExpression(): PrefixUnaryExpression {
	val token = cursor.previous
	val expression = parseExpression()
	if (expression is SuffixUnaryExpression) {
		syntaxError("不能在这里使用后缀操作符 '${expression.operator.kind.value}'", expression.location.end)
	}
	if (expression !is Identifier && expression !is MemberAccessExpression) {
		syntaxError("不能在这里使用前缀操作符 '${token.value}'", token)
	}
	val operator = Operator(token.kind as OperatorKind, token.location)
	return PrefixUnaryExpression(operator, expression)
}