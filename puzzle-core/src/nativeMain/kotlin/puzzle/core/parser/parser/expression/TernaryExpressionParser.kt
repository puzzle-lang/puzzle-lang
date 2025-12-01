package puzzle.core.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.TernaryExpression
import puzzle.core.parser.matcher.expression.parseCompleteExpression

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseTernaryExpression(condition: Expression): TernaryExpression {
    val thenExpression = parseCompleteExpression()
    if (cursor.current.type != PzlTokenType.COLON) {
        syntaxError("三元运算符缺少 ':'", cursor.current)
    }
    cursor.advance()
    val elseExpression = parseCompleteExpression()
    return TernaryExpression(condition, thenExpression, elseExpression)
}