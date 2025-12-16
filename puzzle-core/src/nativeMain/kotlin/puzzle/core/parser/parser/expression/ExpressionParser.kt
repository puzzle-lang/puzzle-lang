package puzzle.core.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.matcher.expression.ExpressionMatcher
import puzzle.core.token.*

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
	BracketKind.End.RPAREN,
	BracketKind.End.RBRACKET,
	BracketKind.End.RBRACE,
	SymbolKind.COLON,
	SeparatorKind.COMMA,
	ControlFlowKind.ELSE,
	SymbolKind.ARROW
)

context(_: PzlContext, cursor: PzlTokenCursor)
private fun isAtExpressionEnd(): Boolean {
	val isEnd = cursor.current.kind == MetaKind.EOF
			|| cursor.match(SeparatorKind.SEMICOLON)
			|| nonConsumableEndTokenTypes.any { cursor.check(it) }
	if (isEnd) return true
	val previous = cursor.previous
	val current = cursor.current
	val previousLine = previous.lineColumn.line
	val currentLine = current.lineColumn.line
	if (previousLine == currentLine) return false
	return previousLine < currentLine &&
			current.kind != OperatorKind.AND &&
			current.kind != OperatorKind.OR
}