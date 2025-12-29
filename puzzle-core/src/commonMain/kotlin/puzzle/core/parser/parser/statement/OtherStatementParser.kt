package puzzle.core.parser.parser.statement

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.Assignment
import puzzle.core.parser.ast.expression.CompoundAssignable
import puzzle.core.parser.ast.expression.CompoundAssignableProxy
import puzzle.core.parser.ast.expression.DirectAssignable
import puzzle.core.parser.ast.statement.CompoundAssignmentStatement
import puzzle.core.parser.ast.statement.DirectAssignmentStatement
import puzzle.core.parser.ast.statement.ExpressionStatement
import puzzle.core.parser.ast.statement.Statement
import puzzle.core.parser.parser.expression.parseExpressionChain
import puzzle.core.token.kinds.AssignmentKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseOtherStatement(): Statement {
	val expression = parseExpressionChain()
	if (cursor.current.kind !is AssignmentKind) {
		return ExpressionStatement(expression)
	}
	cursor.advance()
	val token = cursor.previous
	val kind = token.kind as AssignmentKind
	if (!kind.isCompound) {
		if (expression !is DirectAssignable) {
			syntaxError("不支持使用 '${token.value}' 赋值", token)
		}
		val value = parseExpressionChain()
		val currentKind = cursor.current.kind
		if (currentKind is AssignmentKind) {
			syntaxError(
				message = "语法错误, 不支持使用 '${currentKind.value}' ${if (currentKind.isCompound) "复合赋值" else "赋值"}",
				token = cursor.current
			)
		}
		return DirectAssignmentStatement(
			target = expression,
			assignment = Assignment(kind, token.location),
			value = value,
			location = expression.location span value.location
		)
	}
	var temp = expression
	while (temp is CompoundAssignableProxy) {
		temp = temp.inner
	}
	if (temp !is CompoundAssignable) {
		syntaxError("不支持使用 '${token.value}' 复合赋值", token)
	}
	val value = parseExpressionChain()
	val currentKind = cursor.current.kind
	if (currentKind is AssignmentKind) {
		syntaxError(
			message = "语法错误, 不支持使用 '${currentKind.value}' ${if (currentKind.isCompound) "复合赋值" else "赋值"}",
			token = cursor.current
		)
	}
	return CompoundAssignmentStatement(
		target = expression,
		assignment = Assignment(kind, token.location),
		value = value,
		location = expression.location span value.location
	)
}