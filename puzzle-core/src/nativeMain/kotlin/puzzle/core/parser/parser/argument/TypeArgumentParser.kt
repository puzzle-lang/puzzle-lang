package puzzle.core.parser.parser.argument

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.TypeArgument
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseIdentifierName
import puzzle.core.parser.parser.parseTypeReference
import puzzle.core.token.AssignmentKind
import puzzle.core.token.OperatorKind
import puzzle.core.token.SeparatorKind

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
		parseIdentifierName(IdentifierNameTarget.TYPE_ARGUMENT).also {
			cursor.advance()
		}
	} else null
	val type = parseTypeReference(isSupportedLambdaType = true)
	return TypeArgument(name, type)
}