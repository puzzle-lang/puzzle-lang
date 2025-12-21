package puzzle.core.parser.parser.statement

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.VariableDeclarationStatement
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseExpressionChain
import puzzle.core.parser.parser.expression.parseIdentifier
import puzzle.core.parser.parser.parseTypeReference
import puzzle.core.token.kinds.AssignmentKind.ASSIGN
import puzzle.core.token.kinds.ModifierKind.VAR
import puzzle.core.token.kinds.SymbolKind.COLON

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseVariableDeclarationStatement(): VariableDeclarationStatement {
	val start = cursor.previous.location
	val isMutable = cursor.previous.kind == VAR
	val name = parseIdentifier(IdentifierTarget.VARIABLE)
	val type = if (cursor.match(COLON)) {
		parseTypeReference(allowLambdaType = true)
	} else null
	val initializer = if (cursor.match(ASSIGN)) {
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