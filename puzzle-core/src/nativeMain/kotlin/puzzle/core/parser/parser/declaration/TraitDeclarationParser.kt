package puzzle.core.parser.parser.declaration

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.Declaration
import puzzle.core.parser.ast.declaration.TraitDeclaration
import puzzle.core.parser.matcher.declaration.member.parseMemberDeclaration
import puzzle.core.symbol.Modifier

class TraitDeclarationParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<TraitDeclarationParser>(::TraitDeclarationParser)
	
	context(_: PzlContext)
	fun parse(modifiers: List<Modifier>): TraitDeclaration {
		cursor.expect(PzlTokenType.IDENTIFIER, "特征缺少名称")
		val name = cursor.previous.value
		if (!cursor.match(PzlTokenType.LBRACE)) {
			return TraitDeclaration(
				name = name,
				modifiers = modifiers
			)
		}
		val members = mutableListOf<Declaration>()
		while (!cursor.match(PzlTokenType.RBRACE)) {
			members += parseMemberDeclaration(cursor)
		}
		return TraitDeclaration(
			name = name,
			modifiers = modifiers,
			members = members
		)
	}
}