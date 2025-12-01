package puzzle.core.parser.matcher.declaration

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.declaration.UniqueDeclaration
import puzzle.core.parser.parser.binding.type.TypeTarget
import puzzle.core.parser.parser.declaration.parseUniqueDeclaration
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.symbol.Modifier

object UniqueDeclarationMatcher : DeclarationMatcher<UniqueDeclaration> {

    override val typeTarget = TypeTarget.UNIQUE

    override val memberModifierTarget = ModifierTarget.MEMBER_UNIQUE

    override val topLevelModifierTarget = ModifierTarget.TOP_LEVEL_UNIQUE

    context(cursor: PzlTokenCursor)
    override fun match(): Boolean {
        return cursor.match(PzlTokenType.UNIQUE)
    }

    context(_: PzlContext, cursor: PzlTokenCursor)
    override fun parse(
        typeSpec: TypeSpec?,
        contextSpec: ContextSpec?,
        modifiers: List<Modifier>,
        isMember: Boolean,
    ): UniqueDeclaration = parseUniqueDeclaration(contextSpec, modifiers, isMember)
}