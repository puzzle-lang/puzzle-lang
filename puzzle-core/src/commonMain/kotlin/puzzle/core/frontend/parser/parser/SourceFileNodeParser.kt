package puzzle.core.frontend.parser.parser

import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.ast.SourceFileNode
import puzzle.core.frontend.ast.declaration.ImportDeclaration
import puzzle.core.frontend.parser.parser.declaration.parseDeclarations
import puzzle.core.frontend.parser.parser.declaration.parseImportDeclaration
import puzzle.core.frontend.parser.parser.declaration.parsePackageDeclaration
import puzzle.core.frontend.token.kinds.NamespaceKind.IMPORT

context(context: PzlContext, cursor: PzlTokenCursor)
fun parseSourceFileNode(): SourceFileNode {
	val start = cursor.current.location
	val packageDeclaration = parsePackageDeclaration()
	val importDeclarations = mutableListOf<ImportDeclaration>()
	while (cursor.match(IMPORT)) {
		importDeclarations += parseImportDeclaration()
	}
	val declarations = parseDeclarations()
	val location = start span cursor.previous.location
	return SourceFileNode(
		path = context.sourcePath,
		packageDeclaration = packageDeclaration,
		importDeclarations = importDeclarations,
		declarations = declarations,
		location = location
	)
}