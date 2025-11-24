package puzzle.core.parser.matcher.declaration.toplevel

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.GenericSpec
import puzzle.core.parser.ast.declaration.EnumDeclaration
import puzzle.core.parser.ast.declaration.NodeKind
import puzzle.core.parser.parser.checkModifiers
import puzzle.core.parser.parser.declaration.EnumDeclarationParser
import puzzle.core.symbol.Modifier

object TopLevelEnumDeclarationMatcher : TopLevelDeclarationMatcher<EnumDeclaration> {
	
	override fun match(cursor: PzlTokenCursor): Boolean {
		return cursor.match(PzlTokenType.ENUM)
	}
	
	context(_: PzlContext)
	override fun check(
		cursor: PzlTokenCursor,
		genericSpec: GenericSpec?,
		modifiers: List<Modifier>
	) {
		checkModifiers(cursor, modifiers, NodeKind.ENUM)
	}
	
	context(_: PzlContext)
	override fun parse(
		cursor: PzlTokenCursor,
		genericSpec: GenericSpec?,
		modifiers: List<Modifier>
	): EnumDeclaration {
		return EnumDeclarationParser.of(cursor).parse(modifiers)
	}
}