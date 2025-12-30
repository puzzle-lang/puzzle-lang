package puzzle.core.parser.parser.statement

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.ContextualStatement
import puzzle.core.parser.ast.statement.SuperStatement
import puzzle.core.parser.ast.statement.ThisStatement
import puzzle.core.parser.parser.expression.ArgumentTarget
import puzzle.core.parser.parser.expression.parseArguments
import puzzle.core.token.kinds.ContextualKind.THIS

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseContextualStatement(): ContextualStatement {
	val token = cursor.offset(-2)
	val start = token.location
	val arguments = parseArguments(ArgumentTarget.CONTEXTUAL)
	val end = cursor.previous.location
	return if (token.kind == THIS) {
		ThisStatement(arguments, start span end)
	} else {
		SuperStatement(arguments, end span start)
	}
}