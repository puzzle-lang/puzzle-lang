package puzzle.core.parser.parser.declaration

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.PackageDeclaration
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifierString
import puzzle.core.token.kinds.AccessKind.DOT
import puzzle.core.token.kinds.NamespaceKind.PACKAGE

context(_: PzlContext, cursor: PzlTokenCursor)
fun parsePackageDeclaration(): PackageDeclaration? {
	if (!cursor.match(PACKAGE)) return null
	val start = cursor.previous.location
	val packages = mutableListOf<String>()
	packages += parseIdentifierString(IdentifierTarget.PACKAGE)
	while (cursor.match(DOT)) {
		packages += parseIdentifierString(IdentifierTarget.PACKAGE)
	}
	val end = cursor.previous.location
	return PackageDeclaration(packages, start span end)
}