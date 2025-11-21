package puzzle.core.parser.declaration.parser

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.*
import puzzle.core.parser.declaration.ClassDeclaration
import puzzle.core.parser.declaration.Declaration
import puzzle.core.parser.declaration.TypeKind
import puzzle.core.parser.declaration.matcher.member.parseMemberDeclaration
import puzzle.core.parser.parameter.parser.parseClassParameters

class ClassDeclarationParser(
	private val cursor: PzlTokenCursor,
) {
	
	context(_: PzlContext)
	fun parse(modifiers: Set<Modifier>): ClassDeclaration {
		cursor.expect(PzlTokenType.IDENTIFIER, "类缺少名称")
		val name = cursor.previous.value
		val classAccess = modifiers.access
		val constructorModifiers = mutableSetOf<Modifier>()
		constructorModifiers += getMemberAccessModifier(cursor, classAccess) {
			"主构造函数访问修饰符与类访问修饰符不兼容"
		}
		val parameters = parseClassParameters(cursor, classAccess)
		val superTypes = parseSuperTypes(cursor)
		if (!cursor.match(PzlTokenType.LBRACE)) {
			return ClassDeclaration(
				name = name,
				modifiers = modifiers,
				constructorModifiers = constructorModifiers,
				parameters = parameters,
				superTypes = superTypes
			)
		}
		
		val members = mutableListOf<Declaration>()
		while (!cursor.match(PzlTokenType.RBRACE)) {
			members += parseDeclaration(classAccess, modifiers)
		}
		
		return ClassDeclaration(
			name = name,
			modifiers = modifiers,
			constructorModifiers = constructorModifiers,
			parameters = parameters,
			superTypes = superTypes,
			members = members
		)
	}
	
	context(_: PzlContext)
	private fun parseDeclaration(
		classAccess: Modifier,
		classModifiers: Set<Modifier>,
	): Declaration {
		val memberModifiers = mutableSetOf<Modifier>()
		memberModifiers += getMemberAccessModifier(cursor, classAccess) {
			"访问修饰符与类访问修饰符不兼容"
		}
		memberModifiers += getDeclarationModifiers(cursor)
		return parseMemberDeclaration(
			cursor = cursor,
			parentTypeKind = TypeKind.CLASS,
			parentModifiers = classModifiers,
			modifiers = memberModifiers
		)
	}
}