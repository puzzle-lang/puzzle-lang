package puzzle.core.parser.matcher.declaration

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.parser.ast.declaration.AnnotationDeclaration
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.parser.parser.declaration.parseAnnotationDeclaration
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.symbol.Modifier

object AnnotationDeclarationMatcher : DeclarationMatcher<AnnotationDeclaration> {

    override val typeTarget = TypeTarget.ANNOTATION

    override val memberModifierTarget = ModifierTarget.MEMBER_ANNOTATION

    override val topLevelModifierTarget = ModifierTarget.TOP_LEVEL_ANNOTATION

    context(cursor: PzlTokenCursor)
    override fun match(): Boolean {
        return cursor.match(PzlTokenType.ANNOTATION)
    }

    context(_: PzlContext, cursor: PzlTokenCursor)
    override fun parse(
        typeSpec: TypeSpec?,
        contextSpec: ContextSpec?,
        modifiers: List<Modifier>,
        annotationCalls: List<AnnotationCall>,
        isMember: Boolean
    ): AnnotationDeclaration = parseAnnotationDeclaration(typeSpec, modifiers, annotationCalls)
}