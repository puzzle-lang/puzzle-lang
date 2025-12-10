package puzzle.core.parser.parser.statement

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.VariableDeclarationStatement
import puzzle.core.parser.parser.expression.parseExpressionChain
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseIdentifierName
import puzzle.core.parser.parser.parseTypeReference

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseVariableDeclarationStatement(): VariableDeclarationStatement {
	val isMutable = cursor.previous.type == PzlTokenType.VAR
	val name = parseIdentifierName(IdentifierNameTarget.VARIABLE)
	val type = if (cursor.match(PzlTokenType.COLON)) {
		parseTypeReference(isSupportedLambdaType = true)
	} else null
	val initializer = if (cursor.match(PzlTokenType.ASSIGN)) {
		parseExpressionChain()
	} else null
	return VariableDeclarationStatement(
		name = name,
		type = type,
		isMutable = isMutable,
		initializer = initializer,
	)
}