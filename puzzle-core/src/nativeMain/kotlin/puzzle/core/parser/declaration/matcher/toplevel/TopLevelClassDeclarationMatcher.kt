package puzzle.core.parser.declaration.matcher.toplevel

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.checkSupportedDeclarationModifiers
import puzzle.core.parser.declaration.ClassDeclaration
import puzzle.core.parser.declaration.parser.ClassDeclarationParser

object TopLevelClassDeclarationMatcher : TopLevelDeclarationMatcher<ClassDeclaration> {
	
	override fun match(ctx: PzlParserContext): Boolean {
		return ctx.match(PzlTokenType.CLASS)
	}
	
	context(_: PzlContext)
	override fun check(ctx: PzlParserContext, modifiers: Set<Modifier>) {
		checkSupportedDeclarationModifiers(
			ctx, modifiers, "顶层类",
			isSupportedOpen = true,
			isSupportedAbstract = true
		)
	}
	
	context(_: PzlContext)
	override fun parse(ctx: PzlParserContext, modifiers: Set<Modifier>): ClassDeclaration {
		return ClassDeclarationParser(ctx).parse(modifiers)
	}
}