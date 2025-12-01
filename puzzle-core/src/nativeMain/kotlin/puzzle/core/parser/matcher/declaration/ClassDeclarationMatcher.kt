package puzzle.core.parser.matcher.declaration

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.declaration.ClassDeclaration
import puzzle.core.parser.parser.binding.type.TypeTarget
import puzzle.core.parser.parser.declaration.parseClassDeclaration
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.symbol.Modifier

object ClassDeclarationMatcher : DeclarationMatcher<ClassDeclaration> {

    override val typeTarget = TypeTarget.CLASS

    override val memberModifierTarget = ModifierTarget.MEMBER_CLASS

    override val topLevelModifierTarget = ModifierTarget.TOP_LEVEL_CLASS

    context(cursor: PzlTokenCursor)
    override fun match(): Boolean {
        return cursor.match(PzlTokenType.CLASS)
    }

    context(_: PzlContext, cursor: PzlTokenCursor)
    override fun parse(
        typeSpec: TypeSpec?,
        contextSpec: ContextSpec?,
        modifiers: List<Modifier>,
        isMember: Boolean
    ): ClassDeclaration = parseClassDeclaration(typeSpec, contextSpec, modifiers)
}