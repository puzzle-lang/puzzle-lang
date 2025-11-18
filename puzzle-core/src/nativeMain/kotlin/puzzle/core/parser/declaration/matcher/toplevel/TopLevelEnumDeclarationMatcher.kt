package puzzle.core.parser.declaration.matcher.toplevel

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.checkSupportedDeclarationModifiers
import puzzle.core.parser.declaration.EnumDeclaration
import puzzle.core.parser.declaration.parser.EnumDeclarationParser

object TopLevelEnumDeclarationMatcher : TopLevelDeclarationMatcher<EnumDeclaration> {
	
	override fun match(ctx: PzlParserContext): Boolean {
		return ctx.match(PzlTokenType.ENUM)
	}
	
	context(_: PzlContext)
	override fun check(ctx: PzlParserContext, modifiers: Set<Modifier>) {
		checkSupportedDeclarationModifiers(ctx, modifiers, "顶层枚举")
	}
	
	context(_: PzlContext)
	override fun parse(ctx: PzlParserContext, modifiers: Set<Modifier>): EnumDeclaration {
		return EnumDeclarationParser(ctx).parse(modifiers)
	}
}