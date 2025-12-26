package puzzle.core.parser.parser

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.SourceFileNode
import puzzle.core.parser.ast.declaration.ImportDeclaration
import puzzle.core.parser.parser.declaration.parseDeclarations
import puzzle.core.parser.parser.declaration.parseImportDeclaration
import puzzle.core.parser.parser.declaration.parsePackageDeclaration
import puzzle.core.token.kinds.NamespaceKind.IMPORT

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