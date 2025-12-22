package puzzle.core.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.LoopExpression
import puzzle.core.parser.parser.expression.matchLabel
import puzzle.core.parser.parser.expression.parseLoopExpression
import puzzle.core.token.kinds.ControlFlowKind.LOOP

object LoopExpressionMatcher : ExpressionMatcher, NoPrefixExpressionParser<LoopExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match(LOOP) || cursor.matchLabel(LOOP)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("loop 前不允许有表达式", cursor.previous)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(): LoopExpression {
		return parseLoopExpression()
	}
}