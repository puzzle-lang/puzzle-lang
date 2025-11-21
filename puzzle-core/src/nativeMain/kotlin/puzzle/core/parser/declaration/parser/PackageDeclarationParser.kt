package puzzle.core.parser.declaration.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.declaration.PackageDeclaration

class PackageDeclarationParser(
	private val cursor: PzlTokenCursor,
) {
	
	context(_: PzlContext)
	fun parse(): PackageDeclaration {
		if (cursor.match(PzlTokenType.PACKAGE)) {
			val packages = mutableListOf<String>()
			cursor.expect(PzlTokenType.IDENTIFIER, "package 后应跟包名")
			packages += cursor.previous.value
			
			while (cursor.match(PzlTokenType.DOT)) {
				cursor.expect(PzlTokenType.IDENTIFIER, "'.' 后应跟标识符")
				packages += cursor.previous.value
			}
			return PackageDeclaration(packages)
		}
		syntaxError("文件缺少包定义", cursor.current)
	}
}