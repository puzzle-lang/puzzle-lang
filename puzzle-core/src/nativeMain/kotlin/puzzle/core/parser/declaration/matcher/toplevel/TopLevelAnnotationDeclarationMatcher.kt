package puzzle.core.parser.declaration.matcher.toplevel

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.checkSupportedDeclarationModifiers
import puzzle.core.parser.declaration.AnnotationDeclaration
import puzzle.core.parser.declaration.parser.AnnotationDeclarationParser

object TopLevelAnnotationDeclarationMatcher : TopLevelDeclarationMatcher<AnnotationDeclaration> {
	
	override fun match(ctx: PzlParserContext): Boolean {
		return ctx.match(PzlTokenType.ANNOTATION)
	}
	
	context(_: PzlContext)
	override fun check(ctx: PzlParserContext, modifiers: Set<Modifier>) {
		checkSupportedDeclarationModifiers(ctx, modifiers, "顶层注解")
	}
	
	context(_: PzlContext)
	override fun parse(ctx: PzlParserContext, modifiers: Set<Modifier>): AnnotationDeclaration {
		return AnnotationDeclarationParser(ctx).parse(modifiers)
	}
}