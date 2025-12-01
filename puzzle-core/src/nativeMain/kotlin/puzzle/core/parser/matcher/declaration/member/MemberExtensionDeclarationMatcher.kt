package puzzle.core.parser.matcher.declaration.member

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.declaration.ExtensionDeclaration
import puzzle.core.parser.parser.binding.type.TypeTarget
import puzzle.core.parser.parser.binding.type.check
import puzzle.core.parser.parser.declaration.parseExtensionDeclaration
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.modifier.check
import puzzle.core.symbol.Modifier

object MemberExtensionDeclarationMatcher : MemberDeclarationMatcher<ExtensionDeclaration> {

    context(cursor: PzlTokenCursor)
    override fun match(): Boolean {
        return cursor.match(PzlTokenType.EXTENSION)
    }

    context(_: PzlContext, cursor: PzlTokenCursor)
    override fun parse(
        typeSpec: TypeSpec?,
        contextSpec: ContextSpec?,
        modifiers: List<Modifier>
    ): ExtensionDeclaration {
        typeSpec?.check(TypeTarget.EXTENSION)
        modifiers.check(ModifierTarget.MEMBER_EXTENSION)
        return parseExtensionDeclaration(typeSpec, contextSpec, modifiers)
    }
}