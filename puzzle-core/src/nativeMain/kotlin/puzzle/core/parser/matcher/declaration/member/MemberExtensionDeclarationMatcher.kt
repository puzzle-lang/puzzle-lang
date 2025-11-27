package puzzle.core.parser.matcher.declaration.member

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.GenericSpec
import puzzle.core.parser.ast.declaration.ExtensionDeclaration
import puzzle.core.parser.parser.binding.generic.GenericTarget
import puzzle.core.parser.parser.binding.generic.check
import puzzle.core.parser.parser.declaration.ExtensionDeclarationParser
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.modifier.check
import puzzle.core.symbol.Modifier

object MemberExtensionDeclarationMatcher : MemberDeclarationMatcher<ExtensionDeclaration> {

    override fun match(cursor: PzlTokenCursor): Boolean {
        return cursor.match(PzlTokenType.EXTENSION)
    }

    context(_: PzlContext)
    override fun parse(
        cursor: PzlTokenCursor,
        genericSpec: GenericSpec?,
        contextSpec: ContextSpec?,
        modifiers: List<Modifier>
    ): ExtensionDeclaration {
        genericSpec?.check(cursor, GenericTarget.EXTENSION)
        modifiers.check(cursor, ModifierTarget.MEMBER_EXTENSION)
        return ExtensionDeclarationParser.of(cursor).parse(genericSpec, contextSpec, modifiers)
    }
}