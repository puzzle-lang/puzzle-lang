package puzzle.core.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.parser.expression.checkIdentifier
import puzzle.core.parser.parser.expression.parsePostfixExpression
import puzzle.core.token.kinds.ContextualKind.SUPER
import puzzle.core.token.kinds.ContextualKind.THIS
import puzzle.core.token.kinds.SymbolKind.HASH

object PostfixExpressionMatcher : ExpressionMatcher, NoPrefixExpressionParser<Expression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return when {
			cursor.checkIdentifier() && cursor.offsetOrNull(1)?.kind != HASH -> {
				cursor.advance()
				true
			}
			
			cursor.match { it.kind == THIS || it.kind == SUPER } -> true
			
			else -> false
		}
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("标识符前不允许是标识符", cursor.previous)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(): Expression {
		return parsePostfixExpression()
	}
}