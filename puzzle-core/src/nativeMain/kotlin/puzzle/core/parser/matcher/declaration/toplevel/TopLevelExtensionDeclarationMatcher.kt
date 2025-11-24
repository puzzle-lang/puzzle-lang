package puzzle.core.parser.matcher.declaration.toplevel

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.GenericSpec
import puzzle.core.parser.ast.declaration.ExtensionDeclaration
import puzzle.core.parser.ast.declaration.NodeKind
import puzzle.core.parser.parser.checkModifiers
import puzzle.core.parser.parser.declaration.ExtensionDeclarationParser
import puzzle.core.symbol.Modifier

object TopLevelExtensionDeclarationMatcher : TopLevelDeclarationMatcher<ExtensionDeclaration> {
	
	override fun match(cursor: PzlTokenCursor): Boolean {
		return cursor.match(PzlTokenType.EXTENSION)
	}
	
	context(_: PzlContext)
	override fun check(
		cursor: PzlTokenCursor,
		genericSpec: GenericSpec?,
		modifiers: List<Modifier>
	) {
		checkModifiers(cursor, modifiers, NodeKind.EXTENSION)
	}
	
	context(_: PzlContext)
	override fun parse(
		cursor: PzlTokenCursor,
		genericSpec: GenericSpec?,
		modifiers: List<Modifier>
	): ExtensionDeclaration {
		return ExtensionDeclarationParser.of(cursor).parse(modifiers)
	}
}