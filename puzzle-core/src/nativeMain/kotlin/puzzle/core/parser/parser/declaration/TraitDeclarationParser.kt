package puzzle.core.parser.parser.declaration

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.GenericSpec
import puzzle.core.parser.ast.declaration.TraitDeclaration
import puzzle.core.parser.matcher.declaration.member.parseMemberDeclaration
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.parser.identifier.IdentifierNameParser
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.symbol.Modifier

class TraitDeclarationParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<TraitDeclarationParser>(::TraitDeclarationParser)
	
	context(_: PzlContext)
	fun parse(
		genericSpec: GenericSpec?,
		modifiers: List<Modifier>
	): TraitDeclaration {
		val name = IdentifierNameParser.of(cursor).parse(IdentifierNameTarget.TRAIT)
		val members = if (cursor.match(PzlTokenType.LBRACE)) {
			buildList {
				while (!cursor.match(PzlTokenType.RBRACE)) {
					this += parseMemberDeclaration(cursor)
				}
			}
		} else emptyList()
		return TraitDeclaration(
			name = name,
			modifiers = modifiers,
			genericSpec = genericSpec,
			contextSpec = null,
			members = members
		)
	}
}