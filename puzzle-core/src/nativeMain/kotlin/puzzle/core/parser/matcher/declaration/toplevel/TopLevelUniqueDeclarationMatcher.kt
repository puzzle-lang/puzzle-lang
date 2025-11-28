package puzzle.core.parser.matcher.declaration.toplevel

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.declaration.UniqueDeclaration
import puzzle.core.parser.parser.binding.type.TypeTarget
import puzzle.core.parser.parser.binding.type.check
import puzzle.core.parser.parser.declaration.UniqueDeclarationParser
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.modifier.check
import puzzle.core.symbol.Modifier

object TopLevelUniqueDeclarationMatcher : TopLevelDeclarationMatcher<UniqueDeclaration> {

    override fun match(cursor: PzlTokenCursor): Boolean {
        return cursor.match(PzlTokenType.UNIQUE)
    }

    context(_: PzlContext)
    override fun parse(
	    cursor: PzlTokenCursor,
	    typeSpec: TypeSpec?,
	    contextSpec: ContextSpec?,
	    modifiers: List<Modifier>
    ): UniqueDeclaration {
        typeSpec?.check(cursor, TypeTarget.UNIQUE)
        modifiers.check(cursor, ModifierTarget.TOP_LEVEL_UNIQUE)
        return UniqueDeclarationParser.of(cursor).parse(contextSpec, modifiers)
    }
}