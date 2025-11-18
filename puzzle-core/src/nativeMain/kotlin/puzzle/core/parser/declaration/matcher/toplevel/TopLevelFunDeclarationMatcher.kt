package puzzle.core.parser.declaration.matcher.toplevel

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.checkSupportedDeclarationModifiers
import puzzle.core.parser.declaration.FunDeclaration
import puzzle.core.parser.declaration.parser.FunDeclarationParser

object TopLevelFunDeclarationMatcher : TopLevelDeclarationMatcher<FunDeclaration> {
	
	override fun match(ctx: PzlParserContext): Boolean {
		return ctx.match(PzlTokenType.FUN)
	}
	
	context(_: PzlContext)
	override fun check(ctx: PzlParserContext, modifiers: Set<Modifier>) {
		checkSupportedDeclarationModifiers(
			ctx, modifiers, "顶层函数",
			isSupportedConst = true
		)
	}
	
	context(_: PzlContext)
	override fun parse(ctx: PzlParserContext, modifiers: Set<Modifier>): FunDeclaration {
		return FunDeclarationParser(ctx).parse(modifiers)
	}
}