package puzzle.core.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.*
import puzzle.core.parser.matcher.expression.parseExpressionChain

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseGroupingExpression(): Expression {
    var expression = parseExpressionChain()
    if (cursor.current.type != PzlTokenType.RPAREN) {
        syntaxError("'(' 必须由 ')' 结束", cursor.current)
    }
    cursor.advance()
    expression = if (isOmissible(expression)) expression else GroupingExpression(expression)
    return if (isAccessOperator()) {
        parsePostfixExpression(expression)
    } else expression
}

private fun isOmissible(expression: Expression): Boolean {
    return expression !is BinaryExpression &&
            expression !is PrefixUnaryExpression &&
            expression !is TernaryExpression
}