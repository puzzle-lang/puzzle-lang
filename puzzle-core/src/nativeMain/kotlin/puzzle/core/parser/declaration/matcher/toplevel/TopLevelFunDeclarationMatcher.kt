package puzzle.core.parser.declaration.matcher.toplevel

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.DeclarationModifierType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.checkSupportedDeclarationModifiers
import puzzle.core.parser.declaration.FunDeclaration
import puzzle.core.parser.declaration.parser.FunDeclarationParser

object TopLevelFunDeclarationMatcher : TopLevelDeclarationMatcher<FunDeclaration> {
	
	override fun match(cursor: PzlTokenCursor): Boolean {
		return cursor.match(PzlTokenType.FUN)
	}
	
	context(_: PzlContext)
	override fun check(cursor: PzlTokenCursor, modifiers: Set<Modifier>) {
		checkSupportedDeclarationModifiers(
			cursor, "顶层函数", modifiers,
			supportedTypes = DeclarationModifierType.topFunTypes
		)
	}
	
	context(_: PzlContext)
	override fun parse(cursor: PzlTokenCursor, modifiers: Set<Modifier>): FunDeclaration {
		return FunDeclarationParser(cursor).parse(modifiers)
	}
}