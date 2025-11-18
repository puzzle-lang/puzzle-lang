package puzzle.core.parser.declaration.matcher.toplevel

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.checkSupportedDeclarationModifiers
import puzzle.core.parser.declaration.SingleDeclaration
import puzzle.core.parser.declaration.parser.SingleDeclarationParser

object TopLevelSingleDeclarationMatcher : TopLevelDeclarationMatcher<SingleDeclaration> {
	
	override fun match(ctx: PzlParserContext): Boolean {
		return ctx.match(PzlTokenType.SINGLE)
	}
	
	context(_: PzlContext)
	override fun check(ctx: PzlParserContext, modifiers: Set<Modifier>) {
		checkSupportedDeclarationModifiers(ctx, modifiers, "顶层单例类")
	}
	
	context(_: PzlContext)
	override fun parse(ctx: PzlParserContext, modifiers: Set<Modifier>): SingleDeclaration {
		return SingleDeclarationParser(ctx).parse(modifiers)
	}
}