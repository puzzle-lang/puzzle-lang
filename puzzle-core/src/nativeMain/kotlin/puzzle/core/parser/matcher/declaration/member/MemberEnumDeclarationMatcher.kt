package puzzle.core.parser.matcher.declaration.member

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.EnumDeclaration
import puzzle.core.parser.ast.declaration.NodeKind
import puzzle.core.parser.parser.checkModifiers
import puzzle.core.parser.parser.declaration.EnumDeclarationParser
import puzzle.core.symbol.Modifier

object MemberEnumDeclarationMatcher : MemberDeclarationMatcher<EnumDeclaration> {
	
	override fun match(cursor: PzlTokenCursor): Boolean {
		return cursor.match(PzlTokenType.ENUM)
	}
	
	context(_: PzlContext)
	override fun check(cursor: PzlTokenCursor, modifiers: List<Modifier>) {
		checkModifiers(cursor, modifiers, NodeKind.MEMBER_ENUM)
	}
	
	context(_: PzlContext)
	override fun parse(cursor: PzlTokenCursor, modifiers: List<Modifier>): EnumDeclaration {
		return EnumDeclarationParser.of(cursor).parse(modifiers)
	}
}