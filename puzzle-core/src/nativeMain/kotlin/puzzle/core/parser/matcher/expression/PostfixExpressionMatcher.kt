package puzzle.core.parser.matcher.expression

import puzzle.core.lexer.PzlTokenType.*
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.parser.expression.parsePostfixExpression
import puzzle.core.parser.parser.identifier.matchIdentifierName

object PostfixExpressionMatcher : ExpressionMatcher<Expression> {

    private val tokenTypes = arrayOf(
        NUMBER, STRING, CHAR, TRUE, FALSE,
        THIS, SUPER, NULL,
        DOT, QUESTION_DOT, DOUBLE_COLON
    )

    context(cursor: PzlTokenCursor)
    override fun match(left: Expression?): Boolean {
        return tokenTypes.any { cursor.match(it) } || matchIdentifierName()
    }

    context(_: PzlContext, cursor: PzlTokenCursor)
    override fun parse(left: Expression?): Expression {
        return parsePostfixExpression(left)
    }
}