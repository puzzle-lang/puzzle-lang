package puzzle.core.parser.declaration.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.declaration.Declaration
import puzzle.core.parser.declaration.UniqueDeclaration
import puzzle.core.parser.declaration.matcher.member.parseMemberDeclaration

class UniqueDeclarationParser(
	private val cursor: PzlTokenCursor
) {
	
	context(_: PzlContext)
	fun parse(modifiers: List<Modifier>, isMember: Boolean = false): UniqueDeclaration {
		val name = when {
			cursor.match(PzlTokenType.IDENTIFIER) -> cursor.previous.value
			isMember -> ""
			else -> syntaxError("单例类缺少名称", cursor.current)
		}
		if (!cursor.match(PzlTokenType.LBRACE)) {
			return UniqueDeclaration(
				name = name,
				modifiers = modifiers
			)
		}
		val members = mutableListOf<Declaration>()
		while (!cursor.match(PzlTokenType.RBRACE)) {
			members += parseMemberDeclaration(cursor)
		}
		
		return UniqueDeclaration(
			name = name,
			modifiers = modifiers,
			members = members
		)
	}
}