package puzzle.core.parser.matcher.declaration

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.declaration.StructDeclaration
import puzzle.core.parser.parser.binding.type.TypeTarget
import puzzle.core.parser.parser.declaration.parseStructDeclaration
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.symbol.Modifier

object StructDeclarationMatcher : DeclarationMatcher<StructDeclaration> {

    override val typeTarget = TypeTarget.STRUCT

    override val memberModifierTarget = ModifierTarget.MEMBER_STRUCT

    override val topLevelModifierTarget = ModifierTarget.TOP_LEVEL_STRUCT

    context(cursor: PzlTokenCursor)
    override fun match(): Boolean {
        return cursor.match(PzlTokenType.STRUCT)
    }

    context(_: PzlContext, cursor: PzlTokenCursor)
    override fun parse(
        typeSpec: TypeSpec?,
        contextSpec: ContextSpec?,
        modifiers: List<Modifier>,
        isMember: Boolean
    ): StructDeclaration = parseStructDeclaration(typeSpec, contextSpec, modifiers)
}