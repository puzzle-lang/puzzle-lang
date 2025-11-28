package puzzle.core.parser.matcher.declaration.member

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.declaration.EnumDeclaration
import puzzle.core.parser.parser.binding.type.TypeTarget
import puzzle.core.parser.parser.binding.type.check
import puzzle.core.parser.parser.declaration.EnumDeclarationParser
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.modifier.check
import puzzle.core.symbol.Modifier

object MemberEnumDeclarationMatcher : MemberDeclarationMatcher<EnumDeclaration> {

    override fun match(cursor: PzlTokenCursor): Boolean {
        return cursor.match(PzlTokenType.ENUM)
    }

    context(_: PzlContext)
    override fun parse(
	    cursor: PzlTokenCursor,
	    typeSpec: TypeSpec?,
	    contextSpec: ContextSpec?,
	    modifiers: List<Modifier>
    ): EnumDeclaration {
        typeSpec?.check(cursor, TypeTarget.ENUM)
        modifiers.check(cursor, ModifierTarget.MEMBER_ENUM)
        return EnumDeclarationParser.of(cursor).parse(typeSpec, contextSpec, modifiers)
    }
}