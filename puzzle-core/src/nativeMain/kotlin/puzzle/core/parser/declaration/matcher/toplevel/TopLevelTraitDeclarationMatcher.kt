package puzzle.core.parser.declaration.matcher.toplevel

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.checkSupportedDeclarationModifiers
import puzzle.core.parser.declaration.TraitDeclaration
import puzzle.core.parser.declaration.parser.TraitDeclarationParser

object TopLevelTraitDeclarationMatcher : TopLevelDeclarationMatcher<TraitDeclaration> {
	
	override fun match(cursor: PzlTokenCursor): Boolean {
		return cursor.match(PzlTokenType.TRAIT)
	}
	
	context(_: PzlContext)
	override fun check(cursor: PzlTokenCursor, modifiers: Set<Modifier>) {
		checkSupportedDeclarationModifiers(cursor, modifiers, "顶层特征")
	}
	
	context(_: PzlContext)
	override fun parse(cursor: PzlTokenCursor, modifiers: Set<Modifier>): TraitDeclaration {
		return TraitDeclarationParser(cursor).parse(modifiers)
	}
}