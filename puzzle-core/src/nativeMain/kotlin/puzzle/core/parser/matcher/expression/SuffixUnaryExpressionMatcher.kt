package puzzle.core.parser.matcher.expression

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.SuffixUnaryExpression
import puzzle.core.parser.parser.expression.parseSuffixUnaryExpression

object SuffixUnaryExpressionMatcher : ExpressionMatcher<SuffixUnaryExpression> {

    context(cursor: PzlTokenCursor)
    override fun match(left: Expression?): Boolean {
        val type = cursor.next.type
        return type == PzlTokenType.DOUBLE_PLUS || type == PzlTokenType.DOUBLE_MINUS
    }

    context(_: PzlContext, cursor: PzlTokenCursor)
    override fun parse(left: Expression?): SuffixUnaryExpression {
        return parseSuffixUnaryExpression()
    }
}