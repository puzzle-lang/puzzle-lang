package puzzle.core.parser.matcher.declaration

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.parser.ast.declaration.TraitDeclaration
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.parser.parser.declaration.parseTraitDeclaration
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.symbol.Modifier

object TraitDeclarationMatcher : DeclarationMatcher<TraitDeclaration> {

    override val typeTarget = TypeTarget.TRAIT

    override val memberModifierTarget = ModifierTarget.MEMBER_TRAIT

    override val topLevelModifierTarget = ModifierTarget.TOP_LEVEL_TRAIT

    context(cursor: PzlTokenCursor)
    override fun match(): Boolean {
        return cursor.match(PzlTokenType.TRAIT)
    }

    context(_: PzlContext, cursor: PzlTokenCursor)
    override fun parse(
        typeSpec: TypeSpec?,
        contextSpec: ContextSpec?,
        modifiers: List<Modifier>,
        annotationCalls: List<AnnotationCall>,
        isMember: Boolean
    ): TraitDeclaration = parseTraitDeclaration(typeSpec, contextSpec, modifiers, annotationCalls)
}