package puzzle.core.parser.matcher.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.parser.expression.matchIdentifier
import puzzle.core.parser.parser.expression.parsePostfixExpression
import puzzle.core.token.kinds.AccessKind.*
import puzzle.core.token.kinds.ContextualKind.SUPER
import puzzle.core.token.kinds.ContextualKind.THIS
import puzzle.core.token.kinds.IdentifierKind
import puzzle.core.token.kinds.LiteralKind.*
import puzzle.core.token.kinds.LiteralKind.BooleanKind.FALSE
import puzzle.core.token.kinds.LiteralKind.BooleanKind.TRUE
import puzzle.core.token.kinds.PzlTokenKind

object PostfixExpressionMatcher : ExpressionMatcher<Expression> {
	
	private val kinds: Array<PzlTokenKind> = arrayOf(DOT, QUESTION_DOT, DOUBLE_COLON, TRUE, FALSE, NULL, THIS, SUPER)
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return kinds.any { cursor.match(it) } ||
				cursor.match<NumberKind>() ||
				cursor.match<IdentifierKind>() ||
				cursor.match<StringKind>() ||
				matchIdentifier()
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): Expression {
		return parsePostfixExpression(left)
	}
}