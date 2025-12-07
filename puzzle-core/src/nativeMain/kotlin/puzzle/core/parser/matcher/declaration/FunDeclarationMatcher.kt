package puzzle.core.parser.matcher.declaration

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.parser.ast.declaration.FunDeclaration
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.parser.parser.declaration.parseFunDeclaration
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.symbol.Modifier

object FunDeclarationMatcher : DeclarationMatcher<FunDeclaration> {

    override val typeTarget = TypeTarget.FUN

    override val memberModifierTarget = ModifierTarget.MEMBER_FUN

    override val topLevelModifierTarget = ModifierTarget.TOP_LEVEL_FUN

    context(cursor: PzlTokenCursor)
    override fun match(): Boolean {
        return cursor.match(PzlTokenType.FUN)
    }

    context(_: PzlContext, cursor: PzlTokenCursor)
    override fun parse(
        typeSpec: TypeSpec?,
        contextSpec: ContextSpec?,
        modifiers: List<Modifier>,
        annotationCalls: List<AnnotationCall>,
        isMember: Boolean
    ): FunDeclaration = parseFunDeclaration(typeSpec, contextSpec, modifiers, annotationCalls)
}