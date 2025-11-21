package puzzle.core.parser.declaration.matcher.toplevel

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.checkSupportedDeclarationModifiers
import puzzle.core.parser.declaration.EnumDeclaration
import puzzle.core.parser.declaration.parser.EnumDeclarationParser

object TopLevelEnumDeclarationMatcher : TopLevelDeclarationMatcher<EnumDeclaration> {
	
	override fun match(cursor: PzlTokenCursor): Boolean {
		return cursor.match(PzlTokenType.ENUM)
	}
	
	context(_: PzlContext)
	override fun check(cursor: PzlTokenCursor, modifiers: Set<Modifier>) {
		checkSupportedDeclarationModifiers(cursor, modifiers, "顶层枚举")
	}
	
	context(_: PzlContext)
	override fun parse(cursor: PzlTokenCursor, modifiers: Set<Modifier>): EnumDeclaration {
		return EnumDeclarationParser(cursor).parse(modifiers)
	}
}