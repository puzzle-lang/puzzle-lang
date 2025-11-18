package puzzle.core.parser.declaration.parser

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.declaration.ImportDeclaration
import puzzle.core.parser.declaration.ImportScope

class ImportDeclarationParser(
	private val ctx: PzlParserContext
) {
	
	context(_: PzlContext)
	fun parse(): ImportDeclaration {
		ctx.expect(PzlTokenType.IDENTIFIER, "import 缺少包名")
		val paths = mutableListOf(ctx.previous.value)
		var scope = ImportScope.SINGLE
		var alias: String? = null
		while (ctx.match(PzlTokenType.DOT)) {
			when {
				ctx.match(PzlTokenType.IDENTIFIER) -> {
					paths += ctx.previous.value
					if (ctx.match(PzlTokenType.AS)) {
						ctx.expect(PzlTokenType.IDENTIFIER, "as 后缺少别名")
						alias = ctx.previous.value
						break
					}
				}
				
				ctx.match(PzlTokenType.STAR) -> {
					scope = ImportScope.WILDCARD
					break
				}
				
				ctx.match(PzlTokenType.DOUBLE_STAR) -> {
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