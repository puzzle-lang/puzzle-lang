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
	private val cursor: PzlTokenCursor
) {
	
	context(_: PzlContext)
	fun parse(modifiers: Set<Modifier>): EnumDeclaration {
		cursor.expect(PzlTokenType.IDENTIFIER, "枚举缺少名称")
		val name = cursor.previous.value
		val enumAccess = modifiers.access
		val parameters = parseClassParameters(cursor, enumAccess)
		cursor.expect(PzlTokenType.LBRACE, "枚举缺少 '{'")
		val entries = parseEnumEntries(enumAccess)
		if (cursor.previous.type == PzlTokenType.RBRACE) {
			return EnumDeclaration(
				name = name,
				modifiers = modifiers,
				parameters = parameters,
				entries = entries
			)
		}
		
		val members = mutableListOf<Declaration>()
		while (!cursor.match(PzlTokenType.RBRACE)) {
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
		while (!cursor.match(PzlTokenType.SEMICOLON) && !cursor.match(PzlTokenType.RBRACE)) {
			entries += EnumEntryParser(cursor).parse(enumAccess)
			if (!cursor.check(PzlTokenType.SEMICOLON) && !cursor.check(PzlTokenType.RBRACE)) {
				cursor.match(PzlTokenType.COMMA)
			}
		}
		if (entries.isEmpty()) {
			syntaxError("请至少为枚举设置一个常量", cursor.previous)
		}
		return entries
	}
	
	context(_: PzlContext)
	private fun parseDeclaration(enumAccess: Modifier, enumModifiers: Set<Modifier>): Declaration {
		val memberModifiers = mutableSetOf<Modifier>()
		memberModifiers += getMemberAccessModifier(cursor, enumAccess) {
			"访问修饰符与枚举访问修饰符不兼容"
		}
		memberModifiers += getDeclarationModifiers(cursor)
		return parseMemberDeclaration(
			cursor = cursor,
			parentTypeKind = TypeKind.ENUM,
			parentModifiers = enumModifiers,
			modifiers = memberModifiers
		)
	}
}

private class EnumEntryParser(
	private val cursor: PzlTokenCursor
) {
	
	context(_: PzlContext)
	fun parse(enumAccess: Modifier): EnumEntry {
		cursor.expect(PzlTokenType.IDENTIFIER, "枚举常量缺少名称")
		val name = cursor.previous.value
		if (cursor.match(PzlTokenType.LPAREN)) {
			while (!cursor.match(PzlTokenType.RPAREN)) {
				cursor.advance()
			}
		}
		val members = mutableListOf<Declaration>()
		if (cursor.match(PzlTokenType.LBRACE)) {
			while (!cursor.match(PzlTokenType.RBRACE)) {
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
		memberModifiers += getMemberAccessModifier(cursor, enumAccess) {
			"访问修饰符与枚举访问修饰符不兼容"
		}
		memberModifiers += getDeclarationModifiers(cursor)
		return parseMemberDeclaration(
			cursor = cursor,
			parentTypeKind = TypeKind.ENUM_ENTRY,
			parentModifiers = emptySet(),
			modifiers = memberModifiers
		)
	}
}