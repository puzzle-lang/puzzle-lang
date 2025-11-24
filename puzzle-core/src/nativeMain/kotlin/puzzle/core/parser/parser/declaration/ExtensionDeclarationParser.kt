package puzzle.core.parser.parser.declaration

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.Declaration
import puzzle.core.parser.ast.declaration.ExtensionDeclaration
import puzzle.core.parser.ast.declaration.SuperTrait
import puzzle.core.parser.matcher.declaration.member.parseMemberDeclaration
import puzzle.core.parser.parser.node.TypeReferenceParser
import puzzle.core.symbol.Modifier

class ExtensionDeclarationParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<ExtensionDeclarationParser>(::ExtensionDeclarationParser)
	
	context(_: PzlContext)
	fun parse(modifiers: List<Modifier>): ExtensionDeclaration {
		val extendedType = TypeReferenceParser.of(cursor).parse()
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
			members += parseMemberDeclaration(cursor)
		}
		
		return ExtensionDeclaration(
			extendedType = extendedType,
			modifiers = modifiers,
			superTraits = superTraits,
			members = members,
		)
	}
}