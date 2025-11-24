package puzzle.core.parser.declaration.parser

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlParser
import puzzle.core.parser.PzlParserProvider
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.declaration.ImportDeclaration
import puzzle.core.parser.declaration.ImportScope

class ImportDeclarationParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<ImportDeclarationParser>(::ImportDeclarationParser)
	
	context(_: PzlContext)
	fun parse(): ImportDeclaration {
		cursor.expect(PzlTokenType.IDENTIFIER, "import 缺少包名")
		val paths = mutableListOf(cursor.previous.value)
		var scope = ImportScope.SINGLE
		var alias: String? = null
		while (cursor.match(PzlTokenType.DOT)) {
			when {
				cursor.match(PzlTokenType.IDENTIFIER) -> {
					paths += cursor.previous.value
					if (cursor.match(PzlTokenType.AS)) {
						cursor.expect(PzlTokenType.IDENTIFIER, "as 后缺少别名")
						alias = cursor.previous.value
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
			paths = paths,
			alias = alias,
			scope = scope
		)
	}
}