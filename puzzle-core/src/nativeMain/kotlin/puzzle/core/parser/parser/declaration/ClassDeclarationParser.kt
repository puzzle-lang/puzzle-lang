package puzzle.core.parser.parser.declaration

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.ClassDeclaration
import puzzle.core.parser.ast.declaration.Declaration
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.matcher.declaration.member.parseMemberDeclaration
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.parser.binding.parameter.parseClassParameters
import puzzle.core.parser.parser.modifier.check
import puzzle.core.parser.parser.modifier.parseModifiers
import puzzle.core.symbol.Modifier

class ClassDeclarationParser private constructor(
	private val cursor: PzlTokenCursor,
) : PzlParser {
	
	companion object : PzlParserProvider<ClassDeclarationParser>(::ClassDeclarationParser)
	
	context(_: PzlContext)
	fun parse(modifiers: List<Modifier>): ClassDeclaration {
		cursor.expect(PzlTokenType.IDENTIFIER, "类缺少名称")
		val name = cursor.previous.value
		val constructorModifiers = parseModifiers(cursor)
		constructorModifiers.check(cursor, ModifierTarget.CONSTRUCTOR_FUN)
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