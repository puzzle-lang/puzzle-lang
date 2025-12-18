package puzzle.core.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.matcher.expression.ExpressionMatcher
import puzzle.core.token.kinds.BracketKind
import puzzle.core.token.kinds.ControlFlowKind.ELSE
import puzzle.core.token.kinds.ControlFlowKind.IF
import puzzle.core.token.kinds.MetaKind.EOF
import puzzle.core.token.kinds.OperatorKind.AND
import puzzle.core.token.kinds.OperatorKind.OR
import puzzle.core.token.kinds.PzlTokenKind
import puzzle.core.token.kinds.SeparatorKind.COMMA
import puzzle.core.token.kinds.SeparatorKind.SEMICOLON
import puzzle.core.token.kinds.SymbolKind.ARROW
import puzzle.core.token.kinds.SymbolKind.COLON

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseExpression(left: Expression? = null): Expression {
	val matcher = ExpressionMatcher.matchers.find { it.match(left) }
		?: syntaxError("不支持的表达式", cursor.current)
	return matcher.parse(left)
}

context(_: PzlContext, _: PzlTokenCursor)
fun parseExpressionChain(left: Expression? = null): Expression {
	var expression = left
	while (!isAtExpressionEnd() || expression == null) {
		expression = parseExpression(expression)
	}
	return expression
}

private val nonConsumableEndTokenTypes = arrayOf<PzlTokenKind>(
	COLON,
	COMMA,
	ELSE,
	ARROW,
	IF
)

context(_: PzlContext, cursor: PzlTokenCursor)
private fun isAtExpressionEnd(): Boolean {
	val kind = cursor.current.kind
	if (kind == EOF || kind is BracketKind.End || nonConsumableEndTokenTypes.any { kind == it }) {
		return true
	}
	if (kind == SEMICOLON) {
		cursor.advance()
		return true
	}
	val previousLine = cursor.previous.location.startPosition.line
	val current = cursor.current
	val currentLine = current.location.startPosition.line
	if (previousLine == currentLine) return false
	return previousLine < currentLine &&
			current.kind != AND &&
			current.kind != OR
}