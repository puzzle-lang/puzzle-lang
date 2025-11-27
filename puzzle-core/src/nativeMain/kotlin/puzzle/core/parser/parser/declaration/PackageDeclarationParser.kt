package puzzle.core.parser.parser.declaration

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.PackageDeclaration
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.parser.identifier.IdentifierNameParser
import puzzle.core.parser.parser.identifier.IdentifierNameTarget

class PackageDeclarationParser private constructor(
	private val cursor: PzlTokenCursor,
) : PzlParser {
	
	companion object : PzlParserProvider<PackageDeclarationParser>(::PackageDeclarationParser)
	
	context(_: PzlContext)
	fun parse(): PackageDeclaration {
		if (!cursor.match(PzlTokenType.PACKAGE)) {
			syntaxError("文件缺少包定义", cursor.current)
		}
		val packages = mutableListOf<String>()
		val parser = IdentifierNameParser.of(cursor)
		packages += parser.parse(IdentifierNameTarget.PACKAGE)
		
		while (cursor.match(PzlTokenType.DOT)) {
			packages += parser.parse(IdentifierNameTarget.PACKAGE_DOT)
		}
		return PackageDeclaration(packages)
	}
}