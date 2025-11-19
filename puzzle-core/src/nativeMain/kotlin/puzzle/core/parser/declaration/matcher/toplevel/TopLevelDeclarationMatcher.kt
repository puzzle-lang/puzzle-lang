package puzzle.core.parser.declaration.matcher.toplevel

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.declaration.Declaration

sealed interface TopLevelDeclarationMatcher<out D : Declaration> {
	
	fun match(ctx: PzlParserContext): Boolean
	
	context(_: PzlContext)
	fun check(ctx: PzlParserContext, modifiers: Set<Modifier>)
	
	context(_: PzlContext)
	fun parse(ctx: PzlParserContext, modifiers: Set<Modifier>): D
}

private val matchers = listOf(
	TopLevelFunDeclarationMatcher,
	TopLevelClassDeclarationMatcher,
	TopLevelSingleDeclarationMatcher,
	TopLevelTraitDeclarationMatcher,
	TopLevelStructDeclarationMatcher,
	TopLevelEnumDeclarationMatcher,
	TopLevelAnnotationDeclarationMatcher
)

context(_: PzlContext)
fun parseTopLevelDeclaration(ctx: PzlParserContext, modifiers: Set<Modifier>): Declaration {
	val matcher = matchers.find { it.match(ctx) }
		?: syntaxError("未知的顶层声明", ctx.current)
	matcher.check(ctx, modifiers)
	return matcher.parse(ctx, modifiers)
}