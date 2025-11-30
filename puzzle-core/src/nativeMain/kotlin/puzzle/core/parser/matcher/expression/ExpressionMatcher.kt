package puzzle.core.parser.matcher.expression

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression

sealed interface ExpressionMatcher<out E : Expression> {
	
	fun match(cursor: PzlTokenCursor, left: Expression?): Boolean
	
	context(_: PzlContext)
	fun parse(cursor: PzlTokenCursor, left: Expression?): E
}

private val matchers = arrayOf(
	GroupingExpressionMatcher,
	PrefixUnaryExpressionMatcher,
	SuffixUnaryExpressionMatcher,
	PostfixExpressionMatcher,
	BinaryExpressionMatcher,
	ElvisExpressionMatcher,
	TernaryExpressionMatcher
)

context(_: PzlContext)
fun parseExpression(cursor: PzlTokenCursor, left: Expression? = null): Expression {
	val matcher = matchers.find { it.match(cursor, left) }
		?: syntaxError("不支持的表达式", cursor.current)
	return matcher.parse(cursor, left)
}

context(_: PzlContext)
fun parseCompleteExpression(cursor: PzlTokenCursor): Expression {
	var expression: Expression? = null
	do {
		expression = parseExpression(cursor, expression)
	} while (!isAtExpressionEnd(cursor))
	return expression
}

private val endTokenTypes = setOf(PzlTokenType.SEMICOLON)

private val nonConsumerEndTokenTypes = setOf(
	PzlTokenType.RPAREN,
	PzlTokenType.RBRACKET,
	PzlTokenType.COLON,
	PzlTokenType.COMMA
)

context(_: PzlContext)
private fun isAtExpressionEnd(cursor: PzlTokenCursor): Boolean {
	val exists = endTokenTypes.any { cursor.match(it) } ||
			nonConsumerEndTokenTypes.any { cursor.check(it) }
	if (exists) return true
	val previous = cursor.previous
	val current = cursor.current
	val previousLine = previous.lineColumn.line
	val currentLine = current.lineColumn.line
	if (previousLine == currentLine) return false
	return previousLine < currentLine && current.type != PzlTokenType.AND && current.type != PzlTokenType.OR
}