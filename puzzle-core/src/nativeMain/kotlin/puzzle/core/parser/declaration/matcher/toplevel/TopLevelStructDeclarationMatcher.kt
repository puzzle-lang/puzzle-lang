package puzzle.core.parser.declaration.matcher.toplevel

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.checkModifiers
import puzzle.core.parser.declaration.NodeKind
import puzzle.core.parser.declaration.StructDeclaration
import puzzle.core.parser.declaration.parser.StructDeclarationParser

object TopLevelStructDeclarationMatcher : TopLevelDeclarationMatcher<StructDeclaration> {
	
	override fun match(cursor: PzlTokenCursor): Boolean {
		return cursor.match(PzlTokenType.STRUCT)
	}
	
	context(_: PzlContext)
	override fun check(cursor: PzlTokenCursor, modifiers: List<Modifier>) {
		checkModifiers(cursor, modifiers, NodeKind.STRUCT)
	}
	
	context(_: PzlContext)
	override fun parse(cursor: PzlTokenCursor, modifiers: List<Modifier>): StructDeclaration {
		return StructDeclarationParser(cursor).parse(modifiers)
	}
}