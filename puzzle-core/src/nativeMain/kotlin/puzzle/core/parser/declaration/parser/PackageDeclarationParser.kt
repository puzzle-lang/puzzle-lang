package puzzle.core.parser.declaration.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.declaration.PackageDeclaration

class PackageDeclarationParser(
	private val ctx: PzlParserContext,
) {
	
	context(_: PzlContext)
	fun parse(): PackageDeclaration {
		if (ctx.match(PzlTokenType.PACKAGE)) {
			val packages = mutableListOf<String>()
			ctx.expect(PzlTokenType.IDENTIFIER, "package 后应跟包名")
			packages += ctx.previous.value
			
			while (ctx.match(PzlTokenType.DOT)) {
				ctx.expect(PzlTokenType.IDENTIFIER, "'.' 后应跟标识符")
				packages += ctx.previous.value
			}
			return PackageDeclaration(packages)
		}
		syntaxError("文件缺少包定义", ctx.current)
	}
}