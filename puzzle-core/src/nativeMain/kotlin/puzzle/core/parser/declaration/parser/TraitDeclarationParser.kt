package puzzle.core.parser.declaration.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.access
import puzzle.core.parser.declaration.TraitDeclaration
import puzzle.core.parser.declaration.Declaration
import puzzle.core.parser.declaration.TypeKind
import puzzle.core.parser.declaration.matcher.member.parseMemberDeclaration
import puzzle.core.parser.getDefaultMemberAccessModifier

class TraitDeclarationParser(
	private val cursor: PzlTokenCursor
) {
	
	context(_: PzlContext)
	fun parse(modifiers: Set<Modifier>): TraitDeclaration {
		cursor.expect(PzlTokenType.IDENTIFIER, "特征缺少名称")
		val name = cursor.previous.value
		if (!cursor.match(PzlTokenType.LBRACE)) {
			return TraitDeclaration(
				name = name,
				modifiers = modifiers
			)
		}
		val interfaceAccess = modifiers.access
		val members = mutableListOf<Declaration>()
		while (!cursor.match(PzlTokenType.RBRACE)) {
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
			cursor.match(PzlTokenType.PRIVATE) -> Modifier.PRIVATE
			cursor.match(PzlTokenType.PROTECTED) -> syntaxError("特征内部不允许使用 'protected' 修饰符", cursor.previous)
			cursor.match(PzlTokenType.FILE) -> syntaxError("特征内部不允许使用 'file' 修饰符", cursor.previous)
			cursor.match(PzlTokenType.INTERNAL) -> syntaxError(
				"特征内部不允许使用 'internal' 修饰符",
				cursor.previous
			)
			
			cursor.match(PzlTokenType.MODULE) -> syntaxError(
				"特征内部不允许使用 'module' 修饰符",
				cursor.previous
			)
			
			cursor.match(PzlTokenType.PUBLIC) -> syntaxError(
				"特征内部不允许使用 'public' 修饰符",
				cursor.previous
			)
			
			else -> getDefaultMemberAccessModifier(interfaceAccess)
		}
		return parseMemberDeclaration(
			cursor = cursor,
			parentTypeKind = TypeKind.TRAIT,
			parentModifiers = setOf(interfaceAccess),
			modifiers = setOf(memberAccess)
		)
	}
}