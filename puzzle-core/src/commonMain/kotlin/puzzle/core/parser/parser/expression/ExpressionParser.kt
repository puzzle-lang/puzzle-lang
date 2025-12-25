package puzzle.core.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.matcher.expression.ExpressionMatcher
import puzzle.core.parser.matcher.expression.NoPrefixExpressionParser
import puzzle.core.parser.matcher.expression.OptionalPrefixExpressionParser
import puzzle.core.parser.matcher.expression.RequirePrefixExpressionParser
import puzzle.core.token.kinds.AccessorKind.GET
import puzzle.core.token.kinds.AccessorKind.SET
import puzzle.core.token.kinds.BracketKind.End.*
import puzzle.core.token.kinds.ControlFlowKind.*
import puzzle.core.token.kinds.MetaKind.EOF
import puzzle.core.token.kinds.OperatorKind.AND
import puzzle.core.token.kinds.OperatorKind.OR
import puzzle.core.token.kinds.SeparatorKind.COMMA
import puzzle.core.token.kinds.SeparatorKind.SEMICOLON
import puzzle.core.token.kinds.SymbolKind.ARROW
import puzzle.core.token.kinds.SymbolKind.COLON

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseExpression(left: Expression? = null): Expression {
	val matcher = ExpressionMatcher.matchers.find { it.match(left) }
		?: syntaxError("不支持的表达式", cursor.current)
	return when (matcher) {
		is RequirePrefixExpressionParser<*> -> {
			if (left == null) matcher.prefixError()
			matcher.parse(left)
		}
		
		is NoPrefixExpressionParser<*> -> {
			if (left != null) matcher.prefixError()
			matcher.parse()
		}
		
		is OptionalPrefixExpressionParser<*> -> matcher.parse(left)
	}
}

context(_: PzlContext, _: PzlTokenCursor)
fun parseExpressionChain(left: Expression? = null): Expression {
	var expression = left
	while (!isAtExpressionEnd() || expression == null) {
		expression = parseExpression(expression)
	}
	return expression
}

context(_: PzlContext, _: PzlTokenCursor)
fun tryParseExpressionChain(): Expression? {
	var expression: Expression? = null
	while (!isAtExpressionEnd()) {
		expression = parseExpression(expression)
	}
	return expression
}

private val endKinds = setOf(
	EOF,
	RPAREN,
	RBRACE,
	RBRACKET,
	COLON,
	COMMA,
	ARROW,
	IF,
	ELSE,
	WHILE,
	GET,
	SET
)

context(_: PzlContext, cursor: PzlTokenCursor)
private fun isAtExpressionEnd(): Boolean {
	val kind = cursor.current.kind
	if (kind in endKinds) return true
	if (kind == SEMICOLON) {
		cursor.advance()
		return true
	}
	val previous = cursor.previousOrNull ?: return false
	val previousLine = previous.location.startPosition.line
	val current = cursor.current
	val currentLine = current.location.startPosition.line
	if (previousLine == currentLine) return false
	return previousLine < currentLine &&
			current.kind != AND &&
			current.kind != OR
}