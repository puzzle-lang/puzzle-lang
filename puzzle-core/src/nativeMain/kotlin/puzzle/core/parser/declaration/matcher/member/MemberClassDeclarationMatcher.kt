package puzzle.core.parser.declaration.matcher.member

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.Modifier
import puzzle.core.parser.checkSupportedDeclarationModifiers
import puzzle.core.parser.declaration.ClassDeclaration
import puzzle.core.parser.declaration.TypeKind
import puzzle.core.parser.declaration.parser.ClassDeclarationParser

object MemberClassDeclarationMatcher : MemberDeclarationMatcher<ClassDeclaration> {
	
	override fun match(cursor: PzlTokenCursor): Boolean {
		return cursor.match(PzlTokenType.CLASS)
	}
	
	context(_: PzlContext)
	override fun check(
		cursor: PzlTokenCursor,
		parentTypeKind: TypeKind,
		parentModifiers: Set<Modifier>,
		modifiers: Set<Modifier>
	) {
		checkSupportedDeclarationModifiers(
			cursor, modifiers, name = "内部类",
			isSupportedOpen = true,
			isSupportedAbstract = true
		)
	}
	
	context(_: PzlContext)
	override fun parse(
		cursor: PzlTokenCursor,
		parentTypeKind: TypeKind,
		modifiers: Set<Modifier>
	): ClassDeclaration {
		return ClassDeclarationParser(cursor).parse(modifiers)
	}
}