package puzzle.core.parser.matcher.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression

sealed interface ExpressionMatcher {
	
	companion object {
		
		val matchers = arrayOf(
			InvokeExpressionMatcher,
			GroupingExpressionMatcher,
			NonNullAssertionExpressionMatcher,
			MemberAccessExpressionMatcher,
			MemberReferenceExpressionMatcher,
			PrefixUnaryExpressionMatcher,
			SuffixUnaryExpressionMatcher,
			LiteralExpressionMatcher,
			IdentifierMatcher,
			ContextualExpressionMatcher,
			BinaryExpressionMatcher,
			ElvisExpressionMatcher,
			TernaryExpressionMatcher,
			IsExpressionMatcher,
			AsExpressionMatcher,
			IfExpressionMatcher,
			MatchExpressionMatcher,
			LoopExpressionMatcher,
		)
	}
	
	context(cursor: PzlTokenCursor)
	fun match(left: Expression?): Boolean
}

sealed interface RequirePrefixExpressionParser<out E : Expression> {
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	fun prefixError(): Nothing
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	fun parse(left: Expression): E
}

sealed interface NoPrefixExpressionParser<out E : Expression> {
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	fun prefixError(): Nothing
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	fun parse(): E
}

sealed interface OptionalPrefixExpressionParser<out E : Expression> {
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	fun parse(left: Expression?): E
}