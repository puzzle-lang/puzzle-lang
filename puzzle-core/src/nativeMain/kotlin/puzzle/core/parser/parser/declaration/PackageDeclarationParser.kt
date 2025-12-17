package puzzle.core.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.PackageDeclaration
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifierString
import puzzle.core.token.kinds.AccessKind
import puzzle.core.token.kinds.NamespaceKind
import puzzle.core.model.span

context(_: PzlContext, cursor: PzlTokenCursor)
fun parsePackageDeclaration(): PackageDeclaration {
	if (!cursor.match(NamespaceKind.PACKAGE)) {
		syntaxError("文件缺少包定义", cursor.current)
	}
	val start = cursor.previous.location
	val packages = mutableListOf<String>()
	packages += parseIdentifierString(IdentifierTarget.PACKAGE)
	while (cursor.match(AccessKind.DOT)) {
		packages += parseIdentifierString(IdentifierTarget.PACKAGE_DOT)
	}
	val end = cursor.previous.location
	return PackageDeclaration(packages, start span end)
}