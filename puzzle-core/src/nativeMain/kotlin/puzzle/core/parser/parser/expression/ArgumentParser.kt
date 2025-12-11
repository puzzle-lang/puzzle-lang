package puzzle.core.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Argument
import puzzle.core.parser.ast.expression.InvokeType
import puzzle.core.parser.ast.expression.InvokeType.CALL
import puzzle.core.parser.ast.expression.InvokeType.INDEX_ACCESS
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseIdentifierName
import puzzle.core.token.AssignmentKind
import puzzle.core.token.SeparatorKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseArguments(type: InvokeType): List<Argument> {
	if (cursor.match(type.endTokenKind)) return emptyList()
	val arguments = mutableListOf<Argument>()
	do {
		arguments += parseCallArgument(type)
		if (!cursor.check(type.endTokenKind)) {
			cursor.expect(SeparatorKind.COMMA, "参数缺少 ','")
		}
	} while (!cursor.match(type.endTokenKind))
	return arguments
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseCallArgument(type: InvokeType): Argument {
	val name = if (cursor.offsetOrNull(offset = 1)?.kind == AssignmentKind.ASSIGN) {
		parseIdentifierName(IdentifierNameTarget.ARGUMENT).also {
			cursor.advance()
		}
	} else null
	val expression = parseExpressionChain()
	if (cursor.previous.kind == SeparatorKind.SEMICOLON) {
		syntaxError("语法错误", cursor.previous)
	}
	val currentType = cursor.current.kind
	if (currentType == SeparatorKind.COMMA) {
		return Argument(name, expression)
	}
	when (type) {
		CALL if (currentType == INDEX_ACCESS.endTokenKind) -> syntaxError(
			"函数参数表达式后只允许根 ')' 和 ','",
			cursor.current
		)
		
		INDEX_ACCESS if (currentType == CALL.endTokenKind) -> syntaxError(
			"索引访问参数表达式后只允许根 ']' 和 ','",
			cursor.current
		)
		
		else -> return Argument(name, expression)
	}
}