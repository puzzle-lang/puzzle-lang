package puzzle.core.parser.declaration.matcher.toplevel

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.checkSupportedDeclarationModifiers
import puzzle.core.parser.declaration.StructDeclaration
import puzzle.core.parser.declaration.parser.StructDeclarationParser

object TopLevelStructDeclarationMatcher : TopLevelDeclarationMatcher<StructDeclaration> {
	
	override fun match(ctx: PzlParserContext): Boolean {
		return ctx.match(PzlTokenType.STRUCT)
	}
	
	context(_: PzlContext)
	override fun check(ctx: PzlParserContext, modifiers: Set<Modifier>) {
		checkSupportedDeclarationModifiers(ctx, modifiers, "顶层结构体")
	}
	
	context(_: PzlContext)
	override fun parse(ctx: PzlParserContext, modifiers: Set<Modifier>): StructDeclaration {
		return StructDeclarationParser(ctx).parse(modifiers)
	}
}