package puzzle.core.parser.matcher.declaration.member

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.GenericSpec
import puzzle.core.parser.ast.declaration.UniqueDeclaration
import puzzle.core.parser.parser.binding.generic.GenericTarget
import puzzle.core.parser.parser.binding.generic.check
import puzzle.core.parser.parser.declaration.UniqueDeclarationParser
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.modifier.check
import puzzle.core.symbol.Modifier

object MemberUniqueDeclarationMatcher : MemberDeclarationMatcher<UniqueDeclaration> {

    override fun match(cursor: PzlTokenCursor): Boolean {
        return cursor.match(PzlTokenType.UNIQUE)
    }

    context(_: PzlContext)
    override fun parse(
        cursor: PzlTokenCursor,
        genericSpec: GenericSpec?,
        contextSpec: ContextSpec?,
        modifiers: List<Modifier>
    ): UniqueDeclaration {
        genericSpec?.check(cursor, GenericTarget.UNIQUE)
        modifiers.check(cursor, ModifierTarget.MEMBER_UNIQUE)
        return UniqueDeclarationParser.of(cursor).parse(contextSpec, modifiers, isMember = true)
    }
}