package puzzle.core.parser.parser.statement

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.InitStatement
import puzzle.core.parser.parser.expression.parseArguments
import puzzle.core.parser.parser.type.parseNamedType
import puzzle.core.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.token.kinds.SymbolKind.QUESTION

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseInitStatement(): InitStatement {
	val start = cursor.previous.location
	val isSafe = cursor.match(QUESTION)
	val type = parseNamedType()
	cursor.expect(LPAREN, "${if (isSafe) "init?" else "init"} 语句缺少 '('")
	val arguments = parseArguments()
	val end = cursor.previous.location
	return InitStatement(type, arguments, isSafe, start span end)
}