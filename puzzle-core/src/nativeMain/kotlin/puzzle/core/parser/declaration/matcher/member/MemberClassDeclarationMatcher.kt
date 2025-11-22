package puzzle.core.parser.declaration.matcher.member

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.checkModifiers
import puzzle.core.parser.declaration.ClassDeclaration
import puzzle.core.parser.declaration.NodeKind
import puzzle.core.parser.declaration.parser.ClassDeclarationParser

object MemberClassDeclarationMatcher : MemberDeclarationMatcher<ClassDeclaration> {
	
	override fun match(cursor: PzlTokenCursor): Boolean {
		return cursor.match(PzlTokenType.CLASS)
	}
	
	context(_: PzlContext)
	override fun check(cursor: PzlTokenCursor, modifiers: List<Modifier>) {
		checkModifiers(cursor, modifiers, NodeKind.MEMBER_CLASS)
	}
	
	context(_: PzlContext)
	override fun parse(cursor: PzlTokenCursor, modifiers: List<Modifier>): ClassDeclaration {
		return ClassDeclarationParser(cursor).parse(modifiers)
	}
}