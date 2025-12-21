package puzzle.core.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.Identifier
import puzzle.core.parser.parser.expression.matchIdentifier

object IdentifierMatcher : ExpressionMatcher, NoPrefixExpressionParser<Identifier> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return matchIdentifier()
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("标识符前不允许有表达式", cursor.previous)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(): Identifier {
		val token = cursor.previous
		return Identifier(token.value, token.location)
	}
}