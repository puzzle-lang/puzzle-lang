package puzzle.core.parser.parser

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.SourceFileNode
import puzzle.core.parser.ast.declaration.Declaration
import puzzle.core.parser.ast.declaration.ImportDeclaration
import puzzle.core.parser.matcher.declaration.parseTopLevelDeclaration
import puzzle.core.parser.parser.declaration.parseImportDeclaration
import puzzle.core.parser.parser.declaration.parsePackageDeclaration

context(context: PzlContext, cursor: PzlTokenCursor)
fun parseSourceFileNode(): SourceFileNode {
    val packageDeclaration = parsePackageDeclaration()
    val importDeclarations = mutableListOf<ImportDeclaration>()
    while (cursor.match(PzlTokenType.IMPORT)) {
        importDeclarations += parseImportDeclaration()
    }
    val declarations = mutableListOf<Declaration>()
    while (!cursor.isAtEnd()) {
        declarations += parseTopLevelDeclaration()
    }
    return SourceFileNode(
        path = context.sourcePath,
        packageDeclaration = packageDeclaration,
        importDeclarations = importDeclarations,
        declarations = declarations,
    )
}