package puzzle.core.parser.matcher.declaration

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.declaration.PropertyDeclaration
import puzzle.core.parser.parser.binding.type.TypeTarget
import puzzle.core.parser.parser.declaration.parsePropertyDeclaration
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.symbol.Modifier

object PropertyDeclarationMatcher : DeclarationMatcher<PropertyDeclaration> {

    override val typeTarget = TypeTarget.PROPERTY

    override val memberModifierTarget = ModifierTarget.MEMBER_PROPERTY

    override val topLevelModifierTarget = ModifierTarget.TOP_LEVEL_PROPERTY

    context(cursor: PzlTokenCursor)
    override fun match(): Boolean {
        val type = cursor.previous.type
        return type == PzlTokenType.VAR || type == PzlTokenType.VAL
    }

    context(_: PzlContext, cursor: PzlTokenCursor)
    override fun parse(
        typeSpec: TypeSpec?,
        contextSpec: ContextSpec?,
        modifiers: List<Modifier>,
        isMember: Boolean
    ): PropertyDeclaration = parsePropertyDeclaration(typeSpec, contextSpec, modifiers)
}