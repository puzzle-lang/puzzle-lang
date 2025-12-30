package puzzle.core.parser.parser.statement

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.DestructureVariableSpec
import puzzle.core.parser.ast.statement.SingleVariableSpec
import puzzle.core.parser.ast.statement.Variable
import puzzle.core.parser.ast.statement.VariableDeclarationStatement
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseExpressionChain
import puzzle.core.parser.parser.expression.parseIdentifier
import puzzle.core.parser.parser.type.parseTypeReference
import puzzle.core.token.kinds.AssignmentKind.ASSIGN
import puzzle.core.token.kinds.BracketKind.End.RBRACKET
import puzzle.core.token.kinds.BracketKind.Start.LBRACKET
import puzzle.core.token.kinds.ModifierKind.VAL
import puzzle.core.token.kinds.ModifierKind.VAR
import puzzle.core.token.kinds.SeparatorKind.COMMA
import puzzle.core.token.kinds.SymbolKind.COLON
import puzzle.core.util.isAnonymousBinding

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseVariableDeclarationStatement(): VariableDeclarationStatement {
	val start = cursor.previous.location
	val variableSpec = if (cursor.previous.kind == LBRACKET) {
		parseMultiVariableSpec()
	} else {
		val isMutable = cursor.previous.kind == VAR
		if (cursor.match(LBRACKET)) {
			parseMultiVariableSpec(defaultMutable = isMutable)
		} else {
			parseSingleVariableSpec(isMutable)
		}
	}
	val initializer = if (cursor.match(ASSIGN)) {
		parseExpressionChain()
	} else {
		when (variableSpec) {
			is SingleVariableSpec if variableSpec.variable.type == null -> {
				syntaxError("变量缺少类型", cursor.previous.location.end)
			}
			
			is DestructureVariableSpec -> {
				syntaxError("解构参数必须赋值", cursor.previous.location.end)
			}
			
			else -> null
		}
	}
	val end = cursor.previous.location
	return VariableDeclarationStatement(
		variableSpec = variableSpec,
		initializer = initializer,
		location = start span end
	)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseSingleVariableSpec(isMutable: Boolean): SingleVariableSpec {
	val start = cursor.previous.location
	val name = parseIdentifier(IdentifierTarget.VARIABLE)
	if (name.isAnonymousBinding && isMutable) {
		syntaxError("匿名参数不允许使用 var 可变修饰符", cursor.offset(-2))
	}
	val type = if (cursor.match(COLON)) {
		parseTypeReference(allowLambda = true)
	} else null
	val end = cursor.previous.location
	val variable = Variable(isMutable, name, type, start span end)
	return SingleVariableSpec(variable)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseMultiVariableSpec(defaultMutable: Boolean? = null): DestructureVariableSpec {
	val start = cursor.previous.location
	val varaibles = buildList {
		while (!cursor.match(RBRACKET)) {
			val start = cursor.current.location
			var isMutable = when {
				cursor.match(VAR) -> true
				cursor.match(VAL) -> false
				else -> null
			}
			val name = parseIdentifier(IdentifierTarget.VARIABLE_DESTRUCTURE)
			when {
				isMutable == null -> {
					isMutable = if (name.isAnonymousBinding) false else {
						defaultMutable ?: syntaxError("解构变量缺少可变修饰符", cursor.current)
					}
				}
				
				isMutable && name.isAnonymousBinding -> {
					syntaxError("匿名解构参数不允许使用 var 可变修饰符", cursor.offset(-2))
				}
			}
			val type = if (cursor.match(COLON)) {
				parseTypeReference(allowLambda = true)
			} else null
			val end = cursor.previous.location
			this += Variable(isMutable, name, type, start span end)
			if (!cursor.check(RBRACKET)) {
				cursor.expect(COMMA, "解构变量列表缺少 ','")
			}
		}
	}
	if (varaibles.isEmpty()) {
		syntaxError("解构列表缺少变量", cursor.previous)
	}
	if (varaibles.all { it.name.isAnonymousBinding }) {
		syntaxError("解构列表不允许全部匿名绑定", varaibles.first().name)
	}
	val end = cursor.previous.location
	return DestructureVariableSpec(varaibles, start span end)
}