package puzzle.core.parser.matcher.declaration.member

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.declaration.AnnotationDeclaration
import puzzle.core.parser.parser.binding.type.TypeTarget
import puzzle.core.parser.parser.binding.type.check
import puzzle.core.parser.parser.declaration.AnnotationDeclarationParser
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.modifier.check
import puzzle.core.symbol.Modifier

object MemberAnnotationDeclarationMatcher : MemberDeclarationMatcher<AnnotationDeclaration> {

    override fun match(cursor: PzlTokenCursor): Boolean {
        return cursor.match(PzlTokenType.ANNOTATION)
    }

    context(_: PzlContext)
    override fun parse(
	    cursor: PzlTokenCursor,
	    typeSpec: TypeSpec?,
	    contextSpec: ContextSpec?,
	    modifiers: List<Modifier>
    ): AnnotationDeclaration {
        typeSpec?.check(cursor, TypeTarget.ANNOTATION)
        modifiers.check(cursor, ModifierTarget.MEMBER_ANNOTATION)
        return AnnotationDeclarationParser.of(cursor).parse(typeSpec, modifiers)
    }
}