package puzzle.core.parser.declaration.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.*
import puzzle.core.parser.declaration.Declaration
import puzzle.core.parser.declaration.EnumDeclaration
import puzzle.core.parser.declaration.EnumEntry
import puzzle.core.parser.declaration.TypeKind
import puzzle.core.parser.declaration.matcher.member.parseMemberDeclaration
import puzzle.core.parser.parameter.parser.parseClassParameters

class EnumDeclarationParser(
	private val ctx: PzlParserContext
) {
	
	context(_: PzlContext)
	fun parse(modifiers: Set<Modifier>): EnumDeclaration {
		ctx.expect(PzlTokenType.IDENTIFIER, "枚举缺少名称")
		val name = ctx.previous.value
		val enumAccess = modifiers.access
		val parameters = parseClassParameters(ctx, enumAccess)
		ctx.expect(PzlTokenType.LBRACE, "枚举缺少 '{'")
		val entries = parseEnumEntries(enumAccess)
		if (ctx.previous.type == PzlTokenType.RBRACE) {
			return EnumDeclaration(
				name = name,
				modifiers = modifiers,
				parameters = parameters,
				entries = entries
			)
		}
		
		val members = mutableListOf<Declaration>()
		while (!ctx.match(PzlTokenType.RBRACE)) {
			members += parseDeclaration(enumAccess, modifiers)
		}
		
		return EnumDeclaration(
			name = name,
			modifiers = modifiers,
			parameters = parameters,
			entries = entries,
			members = members
		)
	}
	
	context(_: PzlContext)
	private fun parseEnumEntries(enumAccess: Modifier): List<EnumEntry> {
		val entries = mutableListOf<EnumEntry>()
		while (!ctx.match(PzlTokenType.SEMICOLON) && !ctx.match(PzlTokenType.RBRACE)) {
			entries += EnumEntryParser(ctx).parse(enumAccess)
			if (!ctx.check(PzlTokenType.SEMICOLON) && !ctx.check(PzlTokenType.RBRACE)) {
				ctx.match(PzlTokenType.COMMA)
			}
		}
		if (entries.isEmpty()) {
			syntaxError("请至少为枚举设置一个常量", ctx.previous)
		}
		return entries
	}
	
	context(_: PzlContext)
	private fun parseDeclaration(enumAccess: Modifier, enumModifiers: Set<Modifier>): Declaration {
		val memberModifiers = mutableSetOf<Modifier>()
		memberModifiers += getMemberAccessModifier(ctx, enumAccess) {
			"访问修饰符与枚举访问修饰符不兼容"
		}
		memberModifiers += getDeclarationModifiers(ctx)
		return parseMemberDeclaration(
			ctx = ctx,
			parentTypeKind = TypeKind.ENUM,
			parentModifiers = enumModifiers,
			modifiers = memberModifiers
		)
	}
}

private class EnumEntryParser(
	private val ctx: PzlParserContext
) {
	
	context(_: PzlContext)
	fun parse(enumAccess: Modifier): EnumEntry {
		ctx.expect(PzlTokenType.IDENTIFIER, "枚举常量缺少名称")
		val name = ctx.previous.value
		if (ctx.match(PzlTokenType.LPAREN)) {
			while (!ctx.match(PzlTokenType.RPAREN)) {
				ctx.advance()
			}
		}
		val members = mutableListOf<Declaration>()
		if (ctx.match(PzlTokenType.LBRACE)) {
			while (!ctx.match(PzlTokenType.RBRACE)) {
				members += parseDeclaration(enumAccess)
			}
		}
		return EnumEntry(
			name = name,
			members = members
		)
	}
	
	context(_: PzlContext)
	private fun parseDeclaration(enumAccess: Modifier): Declaration {
		val memberModifiers = mutableSetOf<Modifier>()
		memberModifiers += getMemberAccessModifier(ctx, enumAccess) {
			"访问修饰符与枚举访问修饰符不兼容"
		}
		memberModifiers += getDeclarationModifiers(ctx)
		return parseMemberDeclaration(
			ctx = ctx,
			parentTypeKind = TypeKind.ENUM_ENTRY,
			parentModifiers = emptySet(),
			modifiers = memberModifiers
		)
	}
}