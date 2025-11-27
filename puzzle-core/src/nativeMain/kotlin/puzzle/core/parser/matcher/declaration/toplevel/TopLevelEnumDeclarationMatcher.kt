package puzzle.core.parser.matcher.declaration.toplevel

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.GenericSpec
import puzzle.core.parser.ast.declaration.EnumDeclaration
import puzzle.core.parser.parser.binding.generic.GenericTarget
import puzzle.core.parser.parser.binding.generic.check
import puzzle.core.parser.parser.declaration.EnumDeclarationParser
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.modifier.check
import puzzle.core.symbol.Modifier

object TopLevelEnumDeclarationMatcher : TopLevelDeclarationMatcher<EnumDeclaration> {
	
	override fun match(cursor: PzlTokenCursor): Boolean {
		return cursor.match(PzlTokenType.ENUM)
	}
	
	context(_: PzlContext)
	override fun parse(
		cursor: PzlTokenCursor,
		genericSpec: GenericSpec?,
		modifiers: List<Modifier>
	): EnumDeclaration {
		genericSpec?.check(cursor, GenericTarget.ENUM)
		modifiers.check(cursor, ModifierTarget.TOP_LEVEL_ENUM)
		return EnumDeclarationParser.of(cursor).parse(genericSpec, modifiers)
	}
}