package puzzle.core.parser.parser.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.ContextualExpression
import puzzle.core.parser.ast.expression.SuperExpression
import puzzle.core.parser.ast.expression.ThisExpression
import puzzle.core.token.kinds.ContextualKind.THIS

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseContextualExpression(): ContextualExpression {
	val token = cursor.previous
	return if (token.kind == THIS) {
		ThisExpression(token.location)
	} else {
		SuperExpression(token.location)
	}
}