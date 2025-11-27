package puzzle.core.parser.parser.declaration

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.ImportDeclaration
import puzzle.core.parser.ast.declaration.ImportScope
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.parser.identifier.IdentifierNameParser
import puzzle.core.parser.parser.identifier.IdentifierNameTarget

class ImportDeclarationParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<ImportDeclarationParser>(::ImportDeclarationParser)
	
	context(_: PzlContext)
	fun parse(): ImportDeclaration {
		val parser = IdentifierNameParser.of(cursor)
		val name = parser.parse(IdentifierNameTarget.IMPORT)
		val paths = mutableListOf(name)
		var scope = ImportScope.SINGLE
		var alias: String? = null
		while (cursor.match(PzlTokenType.DOT)) {
			when {
				parser.match() -> {
					paths += cursor.previous.value
					if (cursor.match(PzlTokenType.AS)) {
						alias = parser.parse(IdentifierNameTarget.IMPORT_AS)
						break
					}
				}
				
				cursor.match(PzlTokenType.STAR) -> {
					scope = ImportScope.WILDCARD
					break
				}
				
				cursor.match(PzlTokenType.DOUBLE_STAR) -> {
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
}