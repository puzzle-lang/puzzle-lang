package puzzle.core.parser.declaration.matcher.member

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.*
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
				cursor, modifiers, name = "枚举成员函数",
				isSupportedOpen = true,
				isSupportedAbstract = true,
				isSupportedOverride = true
			)
			
			TypeKind.ENUM_ENTRY -> checkSupportedDeclarationModifiers(
				cursor, modifiers, name = "枚举常量成员函数",
				isSupportedOverride = true
			)
			
			else -> checkSupportedDeclarationModifiers(
				cursor, modifiers, name = "成员函数",
				isSupportedOpen = parentModifiers.isOpen,
				isSupportedAbstract = parentModifiers.isAbstract,
				isSupportedFinalOverride = parentModifiers.isAbstract,
				isSupportedOverride = true,
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