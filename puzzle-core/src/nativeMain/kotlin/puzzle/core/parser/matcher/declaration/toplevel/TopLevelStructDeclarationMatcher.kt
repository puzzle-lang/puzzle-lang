package puzzle.core.parser.matcher.declaration.toplevel

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.GenericSpec
import puzzle.core.parser.ast.declaration.NodeKind
import puzzle.core.parser.ast.declaration.StructDeclaration
import puzzle.core.parser.parser.checkModifiers
import puzzle.core.parser.parser.declaration.StructDeclarationParser
import puzzle.core.symbol.Modifier

object TopLevelStructDeclarationMatcher : TopLevelDeclarationMatcher<StructDeclaration> {
	
	override fun match(cursor: PzlTokenCursor): Boolean {
		return cursor.match(PzlTokenType.STRUCT)
	}
	
	context(_: PzlContext)
	override fun check(
		cursor: PzlTokenCursor,
		genericSpec: GenericSpec?,
		modifiers: List<Modifier>
	) {
		checkModifiers(cursor, modifiers, NodeKind.STRUCT)
	}
	
	context(_: PzlContext)
	override fun parse(
		cursor: PzlTokenCursor,
		genericSpec: GenericSpec?,
		modifiers: List<Modifier>
	): StructDeclaration {
		return StructDeclarationParser.of(cursor).parse(modifiers)
	}
}