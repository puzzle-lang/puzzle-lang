package puzzle.core.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType.*
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression

sealed interface ExpressionMatcher<out E : Expression> {

    context(cursor: PzlTokenCursor)
    fun match(left: Expression?): Boolean

    context(_: PzlContext, cursor: PzlTokenCursor)
    fun parse(left: Expression?): E
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

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseExpression(left: Expression? = null): Expression {
    val matcher = matchers.find { it.match(left) }
        ?: syntaxError("不支持的表达式", cursor.current)
    return matcher.parse(left)
}

context(_: PzlContext, _: PzlTokenCursor)
fun parseExpressionChain(): Expression {
    var expression: Expression? = null
    do {
        expression = parseExpression(expression)
    } while (!isAtExpressionEnd())
    return expression
}

private val nonConsumableEndTokenTypes = setOf(
    RPAREN,
    RBRACKET,
    COLON,
    COMMA
)

context(_: PzlContext, cursor: PzlTokenCursor)
private fun isAtExpressionEnd(): Boolean {
    val isEnd = cursor.current.type == EOF
            || cursor.match(SEMICOLON)
            || nonConsumableEndTokenTypes.any { cursor.check(it) }
    if (isEnd) return true
    val previous = cursor.previous
    val current = cursor.current
    val previousLine = previous.lineColumn.line
    val currentLine = current.lineColumn.line
    if (previousLine == currentLine) return false
    return previousLine < currentLine && current.type != AND && current.type != OR
}