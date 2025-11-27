package puzzle.core.parser.matcher.declaration.member

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.GenericSpec
import puzzle.core.parser.ast.declaration.StructDeclaration
import puzzle.core.parser.parser.binding.generic.GenericTarget
import puzzle.core.parser.parser.binding.generic.check
import puzzle.core.parser.parser.declaration.StructDeclarationParser
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.modifier.check
import puzzle.core.symbol.Modifier

object MemberStructDeclarationMatcher : MemberDeclarationMatcher<StructDeclaration> {

    override fun match(cursor: PzlTokenCursor): Boolean {
        return cursor.match(PzlTokenType.STRUCT)
    }

    context(_: PzlContext)
    override fun parse(
        cursor: PzlTokenCursor,
        genericSpec: GenericSpec?,
        contextSpec: ContextSpec?,
        modifiers: List<Modifier>
    ): StructDeclaration {
        genericSpec?.check(cursor, GenericTarget.STRUCT)
        modifiers.check(cursor, ModifierTarget.MEMBER_STRUCT)
        return StructDeclarationParser.of(cursor).parse(genericSpec, contextSpec, modifiers)
    }
}