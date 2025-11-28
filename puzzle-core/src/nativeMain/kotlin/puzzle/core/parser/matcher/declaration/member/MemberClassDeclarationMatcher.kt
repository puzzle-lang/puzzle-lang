package puzzle.core.parser.matcher.declaration.member

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.declaration.ClassDeclaration
import puzzle.core.parser.parser.binding.type.TypeTarget
import puzzle.core.parser.parser.binding.type.check
import puzzle.core.parser.parser.declaration.ClassDeclarationParser
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.modifier.check
import puzzle.core.symbol.Modifier

object MemberClassDeclarationMatcher : MemberDeclarationMatcher<ClassDeclaration> {

    override fun match(cursor: PzlTokenCursor): Boolean {
        return cursor.match(PzlTokenType.CLASS)
    }

    context(_: PzlContext)
    override fun parse(
	    cursor: PzlTokenCursor,
	    typeSpec: TypeSpec?,
	    contextSpec: ContextSpec?,
	    modifiers: List<Modifier>
    ): ClassDeclaration {
        typeSpec?.check(cursor, TypeTarget.CLASS)
        modifiers.check(cursor, ModifierTarget.MEMBER_CLASS)
        return ClassDeclarationParser.of(cursor).parse(typeSpec, contextSpec, modifiers)
    }
}