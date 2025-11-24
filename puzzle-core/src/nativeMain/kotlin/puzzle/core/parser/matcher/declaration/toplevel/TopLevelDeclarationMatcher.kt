package puzzle.core.parser.matcher.declaration.toplevel

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.GenericSpec
import puzzle.core.parser.ast.declaration.Declaration
import puzzle.core.parser.parser.binding.parseGenericSpec
import puzzle.core.parser.parser.parseModifiers
import puzzle.core.symbol.Modifier

sealed interface TopLevelDeclarationMatcher<out D : Declaration> {
	
	fun match(cursor: PzlTokenCursor): Boolean
	
	context(_: PzlContext)
	fun check(
		cursor: PzlTokenCursor,
		genericSpec: GenericSpec?,
		modifiers: List<Modifier>,
	)
	
	context(_: PzlContext)
	fun parse(
		cursor: PzlTokenCursor,
		genericSpec: GenericSpec?,
		modifiers: List<Modifier>,
	): D
}

private val matchers = listOf(
	TopLevelFunDeclarationMatcher,
	TopLevelClassDeclarationMatcher,
	TopLevelUniqueDeclarationMatcher,
	TopLevelTraitDeclarationMatcher,
	TopLevelStructDeclarationMatcher,
	TopLevelEnumDeclarationMatcher,
	TopLevelAnnotationDeclarationMatcher,
	TopLevelExtensionDeclarationMatcher
)

context(_: PzlContext)
fun parseTopLevelDeclaration(cursor: PzlTokenCursor): Declaration {
	val genericSpec = parseGenericSpec(cursor)
	val modifiers = parseModifiers(cursor)
	val matcher = matchers.find { it.match(cursor) }
		?: syntaxError("未知的顶层声明", cursor.current)
	matcher.check(cursor, genericSpec, modifiers)
	return matcher.parse(cursor, genericSpec, modifiers)
}