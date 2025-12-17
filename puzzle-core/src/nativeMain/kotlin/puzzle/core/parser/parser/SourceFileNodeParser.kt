package puzzle.core.parser.parser

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.SourceFileNode
import puzzle.core.parser.ast.declaration.Declaration
import puzzle.core.parser.ast.declaration.ImportDeclaration
import puzzle.core.parser.parser.declaration.parseImportDeclaration
import puzzle.core.parser.parser.declaration.parsePackageDeclaration
import puzzle.core.parser.parser.declaration.parseTopLevelDeclaration
import puzzle.core.token.kinds.NamespaceKind
import puzzle.core.model.span

context(context: PzlContext, cursor: PzlTokenCursor)
fun parseSourceFileNode(): SourceFileNode {
	val packageDeclaration = parsePackageDeclaration()
	val importDeclarations = mutableListOf<ImportDeclaration>()
	while (cursor.match(NamespaceKind.IMPORT::class)) {
		importDeclarations += parseImportDeclaration()
	}
	val declarations = mutableListOf<Declaration>()
	while (!cursor.isAtEnd()) {
		declarations += parseTopLevelDeclaration()
	}
	val location = packageDeclaration.location span cursor.previous.location
	return SourceFileNode(
		path = context.sourcePath,
		packageDeclaration = packageDeclaration,
		importDeclarations = importDeclarations,
		declarations = declarations,
		location = location
	)
}