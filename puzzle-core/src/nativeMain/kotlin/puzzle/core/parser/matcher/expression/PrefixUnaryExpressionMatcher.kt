package puzzle.core.parser.matcher.expression

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.PrefixUnaryExpression
import puzzle.core.parser.parser.expression.parsePrefixUnaryExpression

object PrefixUnaryExpressionMatcher : ExpressionMatcher<PrefixUnaryExpression> {

    private val tokenTypes = arrayOf(
        PzlTokenType.PLUS,
        PzlTokenType.MINUS,
        PzlTokenType.BANG,
        PzlTokenType.BIT_NOT,
        PzlTokenType.DOUBLE_PLUS,
        PzlTokenType.DOUBLE_MINUS
    )

    context(cursor: PzlTokenCursor)
    override fun match(left: Expression?): Boolean {
        val type = cursor.current.type
        if (type !in tokenTypes) return false
        return if (left == null || type != PzlTokenType.PLUS && type != PzlTokenType.MINUS) {
            cursor.advance()
            true
        } else false
    }

    context(_: PzlContext, cursor: PzlTokenCursor)
    override fun parse(left: Expression?): PrefixUnaryExpression {
        return parsePrefixUnaryExpression()
    }
}