package puzzle.core.parser.declaration.matcher.toplevel

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.checkSupportedDeclarationModifiers
import puzzle.core.parser.declaration.ExtensionDeclaration
import puzzle.core.parser.declaration.parser.ExtensionDeclarationParser

object TopLevelExtensionDeclarationMatcher : TopLevelDeclarationMatcher<ExtensionDeclaration> {
	
	override fun match(cursor: PzlTokenCursor): Boolean {
		return cursor.match(PzlTokenType.EXTENSION)
	}
	
	context(_: PzlContext)
	override fun check(cursor: PzlTokenCursor, modifiers: Set<Modifier>) {
		checkSupportedDeclarationModifiers(
			cursor, modifiers, "扩展"
		)
	}
	
	context(_: PzlContext)
	override fun parse(cursor: PzlTokenCursor, modifiers: Set<Modifier>): ExtensionDeclaration {
		return ExtensionDeclarationParser(cursor).parse(modifiers)
	}
}