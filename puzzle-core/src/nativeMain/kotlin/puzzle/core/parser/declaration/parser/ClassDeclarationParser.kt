package puzzle.core.parser.declaration.parser

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.*
import puzzle.core.parser.binding.parameter.parser.parseClassParameters
import puzzle.core.parser.declaration.ClassDeclaration
import puzzle.core.parser.declaration.Declaration
import puzzle.core.parser.declaration.NodeKind
import puzzle.core.parser.declaration.matcher.member.parseMemberDeclaration

class ClassDeclarationParser private constructor(
	private val cursor: PzlTokenCursor,
) : PzlParser {
	
	companion object : PzlParserProvider<ClassDeclarationParser>(::ClassDeclarationParser)
	
	context(_: PzlContext)
	fun parse(modifiers: List<Modifier>): ClassDeclaration {
		cursor.expect(PzlTokenType.IDENTIFIER, "类缺少名称")
		val name = cursor.previous.value
		val constructorModifiers = parseModifiers(cursor)
		checkModifiers(cursor, constructorModifiers, NodeKind.CONSTRUCTOR_FUN)
		val parameters = parseClassParameters(cursor)
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
			members += parseMemberDeclaration(cursor)
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
}