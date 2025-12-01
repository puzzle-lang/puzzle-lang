package puzzle.core.parser.matcher.declaration

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.declaration.ExtensionDeclaration
import puzzle.core.parser.parser.binding.type.TypeTarget
import puzzle.core.parser.parser.declaration.parseExtensionDeclaration
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.symbol.Modifier

object ExtensionDeclarationMatcher : DeclarationMatcher<ExtensionDeclaration> {

    override val typeTarget = TypeTarget.EXTENSION

    override val memberModifierTarget = ModifierTarget.MEMBER_EXTENSION

    override val topLevelModifierTarget = ModifierTarget.TOP_LEVEL_EXTENSION

    context(cursor: PzlTokenCursor)
    override fun match(): Boolean {
        return cursor.match(PzlTokenType.EXTENSION)
    }

    context(_: PzlContext, cursor: PzlTokenCursor)
    override fun parse(
        typeSpec: TypeSpec?,
        contextSpec: ContextSpec?,
        modifiers: List<Modifier>,
        isMember: Boolean
    ): ExtensionDeclaration = parseExtensionDeclaration(typeSpec, contextSpec, modifiers)
}