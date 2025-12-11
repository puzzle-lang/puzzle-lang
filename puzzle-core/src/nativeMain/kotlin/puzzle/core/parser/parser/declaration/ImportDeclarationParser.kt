package puzzle.core.parser.parser.declaration

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.ImportDeclaration
import puzzle.core.parser.ast.declaration.ImportScope
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.matchIdentifierName
import puzzle.core.parser.parser.identifier.parseIdentifierName
import puzzle.core.token.AccessKind
import puzzle.core.token.ContextualKind
import puzzle.core.token.OperatorKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseImportDeclaration(): ImportDeclaration {
	val name = parseIdentifierName(IdentifierNameTarget.IMPORT)
	val paths = mutableListOf(name)
	var scope = ImportScope.SINGLE
	var alias: String? = null
	while (cursor.match(AccessKind.DOT)) {
		when {
			matchIdentifierName() -> {
				paths += cursor.previous.value
				if (cursor.match(ContextualKind.AS)) {
					alias = parseIdentifierName(IdentifierNameTarget.IMPORT_AS)
					break
				}
			}
			
			cursor.match(OperatorKind.STAR) -> {
				scope = ImportScope.WILDCARD
				break
			}
			
			cursor.match(OperatorKind.DOUBLE_STAR) -> {
				scope = ImportScope.RECURSIVE
				break
			}
		}
	}
	return ImportDeclaration(
		segments = paths,
		alias = alias,
		scope = scope
	)
}