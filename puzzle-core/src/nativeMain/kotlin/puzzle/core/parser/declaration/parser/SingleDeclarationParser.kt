package puzzle.core.parser.declaration.parser

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.*
import puzzle.core.parser.declaration.Declaration
import puzzle.core.parser.declaration.SingleDeclaration
import puzzle.core.parser.declaration.TypeKind
import puzzle.core.parser.declaration.matcher.member.parseMemberDeclaration

class SingleDeclarationParser(
	private val ctx: PzlParserContext
) {
	
	context(_: PzlContext)
	fun parse(modifiers: Set<Modifier>, parentTypeKind: TypeKind? = null): SingleDeclaration {
		val name = when {
			parentTypeKind == null -> {
				ctx.expect(PzlTokenType.IDENTIFIER, "单例类缺少名称")
				ctx.previous.value
			}
			
			parentTypeKind == TypeKind.SINGLE -> {
				ctx.expect(PzlTokenType.IDENTIFIER, "内部单例类不支持默认名称")
				ctx.previous.value
			}
			
			ctx.match(PzlTokenType.IDENTIFIER) -> ctx.previous.value
			
			else -> ""
		}
		val singleAccess = modifiers.access
		if (!ctx.match(PzlTokenType.LBRACE)) {
			return SingleDeclaration(
				name = name,
				modifiers = modifiers
			)
		}
		val members = mutableListOf<Declaration>()
		while (!ctx.match(PzlTokenType.RBRACE)) {
			members += parseDeclaration(singleAccess, modifiers)
		}
		
		return SingleDeclaration(
			name = name,
			modifiers = modifiers,
			members = members
		)
	}
	
	context(_: PzlContext)
	private fun parseDeclaration(
		singleAccess: Modifier,
		singleModifiers: Set<Modifier>,
	): Declaration {
		val memberModifiers = mutableSetOf<Modifier>()
		memberModifiers += getMemberAccessModifier(ctx, singleAccess) {
			"访问修饰符与类访问修饰符不兼容"
		}
		memberModifiers += getDeclarationModifiers(ctx)
		return parseMemberDeclaration(
			ctx = ctx,
			parentTypeKind = TypeKind.SINGLE,
			parentModifiers = singleModifiers,
			modifiers = memberModifiers
		)
	}
}