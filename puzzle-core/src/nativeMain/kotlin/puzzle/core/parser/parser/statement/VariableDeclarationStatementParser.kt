package puzzle.core.parser.parser.statement

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.VariableDeclarationStatement
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseExpressionChain
import puzzle.core.parser.parser.expression.parseIdentifierExpression
import puzzle.core.parser.parser.parseTypeReference
import puzzle.core.token.kinds.AssignmentKind
import puzzle.core.token.kinds.ModifierKind
import puzzle.core.token.kinds.SymbolKind
import puzzle.core.token.span

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseVariableDeclarationStatement(): VariableDeclarationStatement {
	val start = cursor.previous.location
	val isMutable = cursor.previous.kind == ModifierKind.VAR
	val name = parseIdentifierExpression(IdentifierTarget.VARIABLE)
	val type = if (cursor.match(SymbolKind.COLON)) {
		parseTypeReference(isSupportedLambdaType = true)
	} else null
	val initializer = if (cursor.match(AssignmentKind.ASSIGN)) {
		parseExpressionChain()
	} else null
	val end = cursor.previous.location
	return VariableDeclarationStatement(
		name = name,
		type = type,
		isMutable = isMutable,
		initializer = initializer,
		location = start span end
	)
}