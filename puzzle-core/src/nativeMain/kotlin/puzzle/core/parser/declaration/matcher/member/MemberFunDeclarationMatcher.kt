package puzzle.core.parser.declaration.matcher.member

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.*
import puzzle.core.parser.declaration.FunDeclaration
import puzzle.core.parser.declaration.TypeKind
import puzzle.core.parser.declaration.parser.FunDeclarationParser

object MemberFunDeclarationMatcher : MemberDeclarationMatcher<FunDeclaration> {
	
	override fun match(ctx: PzlParserContext): Boolean {
		return ctx.match(PzlTokenType.FUN)
	}
	
	context(_: PzlContext)
	override fun check(
		ctx: PzlParserContext,
		parentTypeKind: TypeKind,
		parentModifiers: Set<Modifier>,
		modifiers: Set<Modifier>
	) {
		when (parentTypeKind) {
			TypeKind.ENUM -> checkSupportedDeclarationModifiers(
				ctx, modifiers, name = "枚举成员函数",
				isSupportedOpen = true,
				isSupportedAbstract = true,
			)
			
			TypeKind.ENUM_ENTRY -> checkSupportedDeclarationModifiers(
				ctx, modifiers, name = "枚举常量成员函数",
				isSupportedOverride = true
			)
			
			else -> checkSupportedDeclarationModifiers(
				ctx, modifiers, name = "成员函数",
				isSupportedOpen = parentModifiers.isOpen,
				isSupportedAbstract = parentModifiers.isAbstract,
				isSupportedFinalOverride = parentModifiers.isAbstract,
				isSupportedOverride = parentModifiers.isAbstract,
			)
		}
	}
	
	context(_: PzlContext)
	override fun parse(
		ctx: PzlParserContext,
		parentTypeKind: TypeKind,
		modifiers: Set<Modifier>
	): FunDeclaration {
		return FunDeclarationParser(ctx).parse(modifiers)
	}
}