package puzzle.core.parser.parser

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.SourceFileNode
import puzzle.core.parser.ast.declaration.Declaration
import puzzle.core.parser.ast.declaration.ImportDeclaration
import puzzle.core.parser.matcher.declaration.toplevel.parseTopLevelDeclaration
import puzzle.core.parser.parser.declaration.ImportDeclarationParser
import puzzle.core.parser.parser.declaration.PackageDeclarationParser

class SourceFileNodeParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	companion object : PzlParserProvider<SourceFileNodeParser>(::SourceFileNodeParser)
	
	context(context: PzlContext)
	fun parse(): SourceFileNode {
		val packageDeclaration = PackageDeclarationParser.of(cursor).parse()
		val importDeclarations = mutableListOf<ImportDeclaration>()
		while (cursor.match(PzlTokenType.IMPORT)) {
			importDeclarations += ImportDeclarationParser.of(cursor).parse()
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