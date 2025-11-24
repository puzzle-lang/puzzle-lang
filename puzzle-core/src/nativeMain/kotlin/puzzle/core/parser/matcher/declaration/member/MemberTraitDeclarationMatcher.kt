package puzzle.core.parser.matcher.declaration.member

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.NodeKind
import puzzle.core.parser.ast.declaration.TraitDeclaration
import puzzle.core.parser.parser.checkModifiers
import puzzle.core.parser.parser.declaration.TraitDeclarationParser
import puzzle.core.symbol.Modifier

object MemberTraitDeclarationMatcher : MemberDeclarationMatcher<TraitDeclaration> {
	
	override fun match(cursor: PzlTokenCursor): Boolean {
		return cursor.match(PzlTokenType.TRAIT)
	}
	
	context(_: PzlContext)
	override fun check(cursor: PzlTokenCursor, modifiers: List<Modifier>) {
		checkModifiers(cursor, modifiers, NodeKind.MEMBER_TRAIT)
	}
	
	context(_: PzlContext)
	override fun parse(cursor: PzlTokenCursor, modifiers: List<Modifier>): TraitDeclaration {
		return TraitDeclarationParser.of(cursor).parse(modifiers)
	}
}