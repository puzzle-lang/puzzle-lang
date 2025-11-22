package puzzle.core.parser.declaration.parser

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.declaration.Declaration
import puzzle.core.parser.declaration.TraitDeclaration
import puzzle.core.parser.declaration.matcher.member.parseMemberDeclaration

class TraitDeclarationParser(
	private val cursor: PzlTokenCursor
) {
	
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