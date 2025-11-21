package puzzle.core.parser.declaration.matcher.toplevel

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.declaration.Declaration

sealed interface TopLevelDeclarationMatcher<out D : Declaration> {
	
	fun match(cursor: PzlTokenCursor): Boolean
	
	context(_: PzlContext)
	fun check(cursor: PzlTokenCursor, modifiers: Set<Modifier>)
	
	context(_: PzlContext)
	fun parse(cursor: PzlTokenCursor, modifiers: Set<Modifier>): D
}

private val matchers = listOf(
	TopLevelFunDeclarationMatcher,
	TopLevelClassDeclarationMatcher,
	TopLevelSingleDeclarationMatcher,
	TopLevelTraitDeclarationMatcher,
	TopLevelStructDeclarationMatcher,
	TopLevelEnumDeclarationMatcher,
	TopLevelAnnotationDeclarationMatcher,
	TopLevelExtensionDeclarationMatcher
)

context(_: PzlContext)
fun parseTopLevelDeclaration(cursor: PzlTokenCursor, modifiers: Set<Modifier>): Declaration {
	val matcher = matchers.find { it.match(cursor) }
		?: syntaxError("未知的顶层声明", cursor.current)
	matcher.check(cursor, modifiers)
	return matcher.parse(cursor, modifiers)
}