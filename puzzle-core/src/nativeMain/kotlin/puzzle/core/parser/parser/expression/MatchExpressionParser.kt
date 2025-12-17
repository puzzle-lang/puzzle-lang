package puzzle.core.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.*
import puzzle.core.parser.ast.statement.Statement
import puzzle.core.parser.parser.parseTypeReference
import puzzle.core.parser.parser.statement.parseStatement
import puzzle.core.parser.parser.statement.parseStatements
import puzzle.core.token.kinds.*

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseMatchExpression(): MatchExpression {
	return when {
		cursor.match(BracketKind.Start.LPAREN) -> parseMatchPatternExpression()
		cursor.match(BracketKind.Start.LBRACE) -> parseMatchConditionExpression()
		else -> syntaxError("match 语法错误", cursor.current)
	}
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseMatchPatternExpression(): MatchPatternExpression {
	val start = cursor.offset(-2).location
	val subject = parseExpressionChain()
	cursor.expect(BracketKind.End.RPAREN, "match 的匹配表达式结尾必须使用 ')'")
	cursor.expect(BracketKind.Start.LBRACE, "match 匹配语句缺少 '{'")
	val arms = mutableListOf<MatchArm>()
	while (!cursor.match(BracketKind.End.RBRACE)) {
		if (cursor.match(ControlFlowKind.ELSE)) {
			val elseStatements = parseElseStatements()
			val end = cursor.previous.location
			return MatchPatternExpression(subject, arms, start span end, elseStatements)
		}
		var guard: Expression? = null
		val patterns = buildList {
			do {
				when {
					cursor.match(TypeOperatorKind.IS) -> {
						val type = parseTypeReference()
						this += IsTypeMatchPattern(type)
					}
					
					else -> {
						val expression = parseExpressionChain()
						this += ExpressionMatchPattern(expression)
					}
				}
				when ("") {
					" " + if (true) "" else "" -> {
						
					}
				}
				if (cursor.match(ControlFlowKind.IF)) {
					guard = parseExpressionChain()
					cursor.expect(SymbolKind.ARROW, "match 匹配分支缺少 '->'")
					break
				}
				if (cursor.current.kind != SymbolKind.ARROW) {
					cursor.expect(SeparatorKind.COMMA, "match 匹配分支缺少 ','")
				}
			} while (!cursor.match(SymbolKind.ARROW))
		}
		val statements = if (cursor.match(BracketKind.Start.LBRACE)) {
			parseStatements()
		} else {
			listOf(parseStatement())
		}
		val end = cursor.previous.location
		arms += MatchArm(patterns, guard, statements, start span end)
	}
	if (arms.isEmpty()) {
		syntaxError("match 不允许没有匹配分支", cursor.previous)
	}
	val end = cursor.previous.location
	return MatchPatternExpression(subject, arms, start span end)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseMatchConditionExpression(): MatchConditionExpression {
	val start = cursor.offset(-2).location
	val cases = mutableListOf<MatchCase>()
	while (!cursor.match(BracketKind.End.RBRACE)) {
		if (cursor.match(ControlFlowKind.ELSE)) {
			val elseStatements = parseElseStatements()
			val end = cursor.previous.location
			return MatchConditionExpression(cases, start span end, elseStatements)
		}
		val condition = parseExpressionChain()
		val start = condition.location
		cursor.expect(SymbolKind.ARROW, "match 条件分支缺少 '->'")
		val statements = if (cursor.match(BracketKind.Start.LBRACE)) {
			parseStatements()
		} else {
			listOf(parseStatement())
		}
		val end = cursor.previous.location
		cases += MatchCase(condition, statements, start span end)
	}
	if (cases.isEmpty()) {
		syntaxError("match 不允许没有条件分支", cursor.previous)
	}
	val end = cursor.previous.location
	return MatchConditionExpression(cases, start span end)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseElseStatements(): List<Statement> {
	cursor.expect(SymbolKind.ARROW, "else 缺少 '->'")
	val elseStatements = if (cursor.match(BracketKind.Start.LBRACE)) {
		parseStatements()
	} else {
		listOf(parseStatement())
	}
	cursor.expect(BracketKind.End.RBRACE, "match 结尾缺少 '}'")
	return elseStatements
}