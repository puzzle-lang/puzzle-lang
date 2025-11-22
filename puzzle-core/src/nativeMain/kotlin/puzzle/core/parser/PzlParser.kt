package puzzle.core.parser

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlToken
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.declaration.Declaration
import puzzle.core.parser.declaration.ImportDeclaration
import puzzle.core.parser.declaration.matcher.toplevel.parseTopLevelDeclaration
import puzzle.core.parser.declaration.parser.ImportDeclarationParser
import puzzle.core.parser.declaration.parser.PackageDeclarationParser
import puzzle.core.parser.node.SourceFileNode

class PzlParser(
	rawTokens: List<PzlToken>
) {
	
	private val cursor = PzlTokenCursor(rawTokens)
	
	context(context: PzlContext)
	fun parse(): SourceFileNode {
		val packageDeclaration = PackageDeclarationParser(cursor).parse()
		val importDeclarations = mutableListOf<ImportDeclaration>()
		while (cursor.match(PzlTokenType.IMPORT)) {
			importDeclarations += ImportDeclarationParser(cursor).parse()
		}
		val declarations = mutableListOf<Declaration>()
		while (!cursor.isAtEnd()) {
			declarations += parseTopLevelDeclaration(cursor)
		}
		return SourceFileNode(
			path = context.sourcePath,
			packageDeclaration = packageDeclaration,
			importDeclarations = importDeclarations,
			declarations = declarations,
		)
	}
}