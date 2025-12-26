package puzzle.core.parser.parser.parameter.parameter

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.parameter.Parameter
import puzzle.core.parser.ast.parameter.Vararg
import puzzle.core.parser.ast.parameter.VarargKind
import puzzle.core.parser.parser.check
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseExpressionChain
import puzzle.core.parser.parser.expression.parseIdentifier
import puzzle.core.parser.parser.parseAnnotationCalls
import puzzle.core.parser.parser.parseModifiers
import puzzle.core.parser.parser.type.parseTypeReference
import puzzle.core.token.kinds.AssignmentKind.ASSIGN
import puzzle.core.token.kinds.BracketKind.End.RPAREN
import puzzle.core.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.token.kinds.OperatorKind.PLUS
import puzzle.core.token.kinds.OperatorKind.STAR
import puzzle.core.token.kinds.SeparatorKind.COMMA
import puzzle.core.token.kinds.SymbolKind.COLON

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseParameters(target: ParameterTarget): List<Parameter> {
	if (!cursor.match(LPAREN)) {
		if (target.allowWithoutParen) return emptyList()
		syntaxError("${target.label} 缺少 '('", cursor.current)
	}
	if (cursor.match(RPAREN)) return emptyList()
	val parameters = buildList {
		do {
			this += parseParameter(target)
			if (!cursor.check(RPAREN)) {
				cursor.expect(COMMA, "型参缺少 ','")
			}
		} while (!cursor.match(RPAREN))
	}
	parameters.check()
	return parameters
}

context(_: PzlContext)
private fun List<Parameter>.check() {
	this.forEachIndexed { index, parameter ->
		val vararg = parameter.vararg ?: return@forEachIndexed
		if (index < this.lastIndex) {
			syntaxError("只允许在最后一个参数使用可变量词", vararg)
		}
	}
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseParameter(target: ParameterTarget): Parameter {
	val start = cursor.current.location
	val annotationCalls = parseAnnotationCalls()
	val modifiers = parseModifiers()
	modifiers.check(target.modifierTarget)
	val name = parseIdentifier(IdentifierTarget.PARAMETER)
	cursor.expect(COLON, "型参缺少 ':'")
	val type = parseTypeReference(allowLambdaType = target.allowLambdaType)
	val vararg = when {
		cursor.match(STAR) -> Vararg(VarargKind.STAR, cursor.previous.location)
		cursor.match(PLUS) -> Vararg(VarargKind.PLUS, cursor.previous.location)
		else -> null
	}
	if (vararg != null && !target.allowVarargQuantifier) {
		syntaxError("${target.label} 参数不支持可变量词", cursor.previous)
	}
	val defaultExpression = if (cursor.match(ASSIGN)) {
		if (vararg != null) {
			syntaxError("${target.label} 可变参数不允许设置默认值", cursor.previous)
		}
		parseExpressionChain()
	} else null
	val end = cursor.previous.location
	return Parameter(
		name = name,
		modifiers = modifiers,
		type = type,
		annotationCalls = annotationCalls,
		vararg = vararg,
		defaultExpression = defaultExpression,
		location = start span end
	)
}