package puzzle.core.parser.parser.argument

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.argument.TypeArgument
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifier
import puzzle.core.parser.parser.type.parseTypeReference
import puzzle.core.token.kinds.AssignmentKind.ASSIGN
import puzzle.core.token.kinds.OperatorKind.GT
import puzzle.core.token.kinds.OperatorKind.LT
import puzzle.core.token.kinds.SeparatorKind.COMMA

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseTypeArguments(): List<TypeArgument> {
	if (!cursor.match(LT)) {
		return emptyList()
	}
	val arguments = mutableListOf<TypeArgument>()
	do {
		arguments += parseTypeArgument()
		if (!cursor.check(GT)) {
			cursor.expect(COMMA, "泛型参数缺少 ','")
		}
	} while (!cursor.match(GT))
	return arguments
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseTypeArgument(): TypeArgument {
	val name = if (cursor.offsetOrNull(offset = 1)?.kind == ASSIGN) {
		parseIdentifier(IdentifierTarget.TYPE_ARGUMENT).also {
			cursor.advance()
		}
	} else null
	val type = parseTypeReference(allowLambda = true)
	return TypeArgument(name, type)
}