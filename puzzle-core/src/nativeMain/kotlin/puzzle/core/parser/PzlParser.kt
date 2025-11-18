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
	
	private val ctx = PzlParserContext(rawTokens)
	
	context(context: PzlContext)
	fun parse(): SourceFileNode {
		val packageDeclaration = PackageDeclarationParser(ctx).parse()
		val importDeclarations = mutableListOf<ImportDeclaration>()
		while (ctx.match(PzlTokenType.IMPORT)) {
			importDeclarations += ImportDeclarationParser(ctx).parse()
		}
		val declarations = mutableListOf<Declaration>()
		while (!ctx.isAtEnd()) {
			declarations += parseDeclaration()
		}
		return SourceFileNode(
			path = context.sourcePath,
			packageDeclaration = packageDeclaration,
			importDeclarations = importDeclarations,
			declarations = declarations,
		)
	}
	
	context(_: PzlContext)
	private fun parseDeclaration(): Declaration {
		val modifiers = mutableSetOf<Modifier>()
		modifiers += getTopLevelAccessModifier(ctx)
		modifiers += getDeclarationModifiers(ctx)
		return parseTopLevelDeclaration(ctx, modifiers)
	}
}