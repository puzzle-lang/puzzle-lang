package puzzle.core.parser.declaration.parser

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.*
import puzzle.core.parser.declaration.Declaration
import puzzle.core.parser.declaration.ExtensionDeclaration
import puzzle.core.parser.declaration.SuperTrait
import puzzle.core.parser.declaration.TypeKind
import puzzle.core.parser.declaration.matcher.member.parseMemberDeclaration
import puzzle.core.parser.node.parser.TypeReferenceParser

class ExtensionDeclarationParser(
	private val cursor: PzlTokenCursor
) {
	
	context(_: PzlContext)
	fun parse(modifiers: Set<Modifier>): ExtensionDeclaration {
		val extendedType = TypeReferenceParser(cursor).parse(isSupportedLambdaType = false)
		val superTraits = parseSuperTypes(cursor, isSupportedClass = false)
			.filterIsInstance<SuperTrait>()
		
		if (!cursor.match(PzlTokenType.LBRACE)) {
			return ExtensionDeclaration(
				extendedType = extendedType,
				modifiers = modifiers,
				superTraits = superTraits,
			)
		}
		
		val members = mutableListOf<Declaration>()
		while (!cursor.match(PzlTokenType.RBRACE)) {
			members += parseDeclaration(modifiers)
		}
		
		return ExtensionDeclaration(
			extendedType = extendedType,
			modifiers = modifiers,
			superTraits = superTraits,
			members = members,
		)
	}
	
	context(_: PzlContext)
	private fun parseDeclaration(extensionModifiers: Set<Modifier>): Declaration {
		val memberModifiers = mutableSetOf<Modifier>()
		memberModifiers += getMemberAccessModifier(cursor, extensionModifiers.access) {
			"访问修饰符与扩展访问修饰符不兼容"
		}
		memberModifiers += getDeclarationModifiers(cursor)
		return parseMemberDeclaration(
			cursor = cursor,
			parentTypeKind = TypeKind.EXTENSION,
			parentModifiers = extensionModifiers,
			modifiers = memberModifiers
		)
	}
}