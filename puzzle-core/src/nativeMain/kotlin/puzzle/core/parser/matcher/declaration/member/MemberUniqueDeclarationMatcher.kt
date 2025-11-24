package puzzle.core.parser.matcher.declaration.member

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.NodeKind
import puzzle.core.parser.ast.declaration.UniqueDeclaration
import puzzle.core.parser.parser.checkModifiers
import puzzle.core.parser.parser.declaration.UniqueDeclarationParser
import puzzle.core.symbol.Modifier

object MemberUniqueDeclarationMatcher : MemberDeclarationMatcher<UniqueDeclaration> {
	
	override fun match(cursor: PzlTokenCursor): Boolean {
		return cursor.match(PzlTokenType.UNIQUE)
	}
	
	context(_: PzlContext)
	override fun check(cursor: PzlTokenCursor, modifiers: List<Modifier>) {
		checkModifiers(cursor, modifiers, NodeKind.MEMBER_SINGLE)
	}
	
	context(_: PzlContext)
	override fun parse(cursor: PzlTokenCursor, modifiers: List<Modifier>): UniqueDeclaration {
		return UniqueDeclarationParser.of(cursor).parse(modifiers, isMember = true)
	}
}