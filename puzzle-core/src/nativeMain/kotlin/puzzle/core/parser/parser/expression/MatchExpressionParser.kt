package puzzle.core.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.MatchCase
import puzzle.core.parser.ast.expression.MatchConditionExpression
import puzzle.core.parser.ast.expression.MatchExpression
import puzzle.core.parser.ast.expression.MatchPatternExpression
import puzzle.core.parser.parser.statement.parseStatement
import puzzle.core.parser.parser.statement.parseStatements
import puzzle.core.token.kinds.BracketKind
import puzzle.core.token.kinds.ControlFlowKind
import puzzle.core.token.kinds.SymbolKind
import puzzle.core.token.span

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
	TODO()
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseMatchConditionExpression(): MatchConditionExpression {
	val start = cursor.offset(-2).location
	val cases = mutableListOf<MatchCase>()
	while (!cursor.match(BracketKind.End.RBRACE)) {
		if (cursor.match(ControlFlowKind.ELSE)) {
			cursor.expect(SymbolKind.ARROW, "else 缺少 '->'")
			val elseStatements = if (cursor.match(BracketKind.Start.LBRACE)) {
				parseStatements()
			} else {
				listOf(parseStatement())
			}
			cursor.expect(BracketKind.End.RBRACE, "match 结尾缺少 '}'")
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