package puzzle.core.parser.declaration.matcher.member

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.DeclarationModifierType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.checkSupportedDeclarationModifiers
import puzzle.core.parser.declaration.FunDeclaration
import puzzle.core.parser.declaration.TypeKind
import puzzle.core.parser.declaration.parser.FunDeclarationParser

object MemberFunDeclarationMatcher : MemberDeclarationMatcher<FunDeclaration> {
	
	override fun match(cursor: PzlTokenCursor): Boolean {
		return cursor.match(PzlTokenType.FUN)
	}
	
	context(_: PzlContext)
	override fun check(
		cursor: PzlTokenCursor,
		parentTypeKind: TypeKind,
		parentModifiers: Set<Modifier>,
		modifiers: Set<Modifier>
	) {
		when (parentTypeKind) {
			TypeKind.ENUM -> checkSupportedDeclarationModifiers(
				cursor, "枚举成员函数", modifiers,
				supportedTypes = DeclarationModifierType.memberFunTypes
			)
			
			TypeKind.ENUM_ENTRY -> checkSupportedDeclarationModifiers(
				cursor, "枚举常量成员函数", modifiers,
				supportedTypes = DeclarationModifierType.enumEntryFunTypes,
			)
			
			else -> checkSupportedDeclarationModifiers(
				cursor, "成员函数", modifiers,
				supportedTypes = DeclarationModifierType.memberFunTypes
			)
		}
	}
	
	context(_: PzlContext)
	override fun parse(
		cursor: PzlTokenCursor,
		parentTypeKind: TypeKind,
		modifiers: Set<Modifier>
	): FunDeclaration {
		return FunDeclarationParser(cursor).parse(modifiers)
	}
}