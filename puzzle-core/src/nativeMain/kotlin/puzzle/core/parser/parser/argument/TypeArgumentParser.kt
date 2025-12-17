package puzzle.core.parser.parser.argument

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.TypeArgument
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifierExpression
import puzzle.core.parser.parser.parseTypeReference
import puzzle.core.token.kinds.AssignmentKind
import puzzle.core.token.kinds.OperatorKind
import puzzle.core.token.kinds.SeparatorKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseTypeArguments(): List<TypeArgument> {
	if (!cursor.match(OperatorKind.LT)) {
		return emptyList()
	}
	val arguments = mutableListOf<TypeArgument>()
	do {
		arguments += parseTypeArgument()
		if (!cursor.check(OperatorKind.GT)) {
			cursor.expect(SeparatorKind.COMMA, "泛型参数缺少 ','")
		}
	} while (!cursor.match(OperatorKind.GT))
	return arguments
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseTypeArgument(): TypeArgument {
	val name = if (cursor.offsetOrNull(offset = 1)?.kind == AssignmentKind.ASSIGN) {
		parseIdentifierExpression(IdentifierTarget.TYPE_ARGUMENT).also {
			cursor.advance()
		}
	} else null
	val type = parseTypeReference(isSupportedLambdaType = true)
	return TypeArgument(name, type)
}