package puzzle.core.parser.parser.declaration

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.ImportDeclaration
import puzzle.core.parser.ast.declaration.ImportScope
import puzzle.core.parser.ast.expression.Identifier
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.matchIdentifier
import puzzle.core.parser.parser.expression.parseIdentifier
import puzzle.core.parser.parser.expression.parseIdentifierString
import puzzle.core.model.span
import puzzle.core.token.kinds.AccessKind.DOT
import puzzle.core.token.kinds.OperatorKind.DOUBLE_STAR
import puzzle.core.token.kinds.OperatorKind.STAR
import puzzle.core.token.kinds.TypeOperatorKind.AS

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseImportDeclaration(): ImportDeclaration {
	val start = cursor.previous.location
	val name = parseIdentifierString(IdentifierTarget.IMPORT)
	val segments = mutableListOf(name)
	var scope = ImportScope.SINGLE
	var alias: Identifier? = null
	while (cursor.match(DOT)) {
		when {
			matchIdentifier() -> {
				segments += cursor.previous.value
				if (cursor.match(AS)) {
					alias = parseIdentifier(IdentifierTarget.IMPORT_AS)
					break
				}
			}
			
			cursor.match(STAR) -> {
				scope = ImportScope.WILDCARD
				break
			}
			
			cursor.match(DOUBLE_STAR) -> {
				scope = ImportScope.RECURSIVE
				break
			}
		}
	}
	val end = cursor.previous.location
	return ImportDeclaration(
		segments = segments,
		alias = alias,
		scope = scope,
		location = start span end
	)
}