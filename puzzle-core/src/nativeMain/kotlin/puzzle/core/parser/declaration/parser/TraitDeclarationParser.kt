package puzzle.core.parser.declaration.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.access
import puzzle.core.parser.declaration.TraitDeclaration
import puzzle.core.parser.declaration.Declaration
import puzzle.core.parser.declaration.TypeKind
import puzzle.core.parser.declaration.matcher.member.parseMemberDeclaration
import puzzle.core.parser.getDefaultMemberAccessModifier

class TraitDeclarationParser(
	private val ctx: PzlParserContext
) {
	
	context(_: PzlContext)
	fun parse(modifiers: Set<Modifier>): TraitDeclaration {
		ctx.expect(PzlTokenType.IDENTIFIER, "特征缺少名称")
		val name = ctx.previous.value
		if (!ctx.match(PzlTokenType.LBRACE)) {
			return TraitDeclaration(
				name = name,
				modifiers = modifiers
			)
		}
		val interfaceAccess = modifiers.access
		val members = mutableListOf<Declaration>()
		while (!ctx.match(PzlTokenType.RBRACE)) {
			members += parseDeclaration(interfaceAccess)
		}
		return TraitDeclaration(
			name = name,
			modifiers = modifiers,
			members = members
		)
	}
	
	context(_: PzlContext)
	private fun parseDeclaration(
		interfaceAccess: Modifier
	): Declaration {
		val memberAccess = when {
			ctx.match(PzlTokenType.PRIVATE) -> Modifier.PRIVATE
			ctx.match(PzlTokenType.PROTECTED) -> syntaxError("特征内部不允许使用 'protected' 修饰符", ctx.previous)
			ctx.match(PzlTokenType.FILE) -> syntaxError("特征内部不允许使用 'file' 修饰符", ctx.previous)
			ctx.match(PzlTokenType.INTERNAL) -> syntaxError(
				"特征内部不允许使用 'internal' 修饰符",
				ctx.previous
			)
			
			ctx.match(PzlTokenType.MODULE) -> syntaxError(
				"特征内部不允许使用 'module' 修饰符",
				ctx.previous
			)
			
			ctx.match(PzlTokenType.PUBLIC) -> syntaxError(
				"特征内部不允许使用 'public' 修饰符",
				ctx.previous
			)
			
			else -> getDefaultMemberAccessModifier(interfaceAccess)
		}
		return parseMemberDeclaration(
			ctx = ctx,
			parentTypeKind = TypeKind.TRAIT,
			parentModifiers = setOf(interfaceAccess),
			modifiers = setOf(memberAccess)
		)
	}
}