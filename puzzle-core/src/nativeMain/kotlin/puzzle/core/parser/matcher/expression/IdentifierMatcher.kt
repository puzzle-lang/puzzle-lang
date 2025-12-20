package puzzle.core.parser.matcher.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.Identifier
import puzzle.core.parser.parser.expression.matchIdentifier

object IdentifierMatcher : ExpressionMatcher<Identifier> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return matchIdentifier()
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): Identifier {
		val token = cursor.previous
		return Identifier(token.value, token.location)
	}
}