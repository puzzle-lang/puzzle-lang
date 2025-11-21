package puzzle.core.parser.declaration.matcher.toplevel

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.DeclarationModifierType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.checkSupportedDeclarationModifiers
import puzzle.core.parser.declaration.ClassDeclaration
import puzzle.core.parser.declaration.parser.ClassDeclarationParser

object TopLevelClassDeclarationMatcher : TopLevelDeclarationMatcher<ClassDeclaration> {
	
	override fun match(cursor: PzlTokenCursor): Boolean {
		return cursor.match(PzlTokenType.CLASS)
	}
	
	context(_: PzlContext)
	override fun check(cursor: PzlTokenCursor, modifiers: Set<Modifier>) {
		checkSupportedDeclarationModifiers(
			cursor, "顶层类", modifiers,
			supportedTypes = DeclarationModifierType.topClassTypes
		)
	}
	
	context(_: PzlContext)
	override fun parse(cursor: PzlTokenCursor, modifiers: Set<Modifier>): ClassDeclaration {
		return ClassDeclarationParser(cursor).parse(modifiers)
	}
}