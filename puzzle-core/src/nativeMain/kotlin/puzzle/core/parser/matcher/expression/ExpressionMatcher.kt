package puzzle.core.parser.matcher.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression

sealed interface ExpressionMatcher<out E : Expression> {
	
	companion object {
		
		val matchers = arrayOf(
			GroupingExpressionMatcher,
			PrefixUnaryExpressionMatcher,
			SuffixUnaryExpressionMatcher,
			PostfixExpressionMatcher,
			BinaryExpressionMatcher,
			ElvisExpressionMatcher,
			TernaryExpressionMatcher,
			IfExpressionMatcher,
			MatchExpressionMatcher,
			IsExpressionMatcher,
			AsExpressionMatcher
		)
	}
	
	context(cursor: PzlTokenCursor)
	fun match(left: Expression?): Boolean
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	fun parse(left: Expression?): E
}