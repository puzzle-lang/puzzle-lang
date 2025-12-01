package puzzle.core.parser.matcher.declaration.toplevel

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.declaration.EnumDeclaration
import puzzle.core.parser.parser.binding.type.TypeTarget
import puzzle.core.parser.parser.binding.type.check
import puzzle.core.parser.parser.declaration.parseEnumDeclaration
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.modifier.check
import puzzle.core.symbol.Modifier

object TopLevelEnumDeclarationMatcher : TopLevelDeclarationMatcher<EnumDeclaration> {

    context(cursor: PzlTokenCursor)
    override fun match(): Boolean {
        return cursor.match(PzlTokenType.ENUM)
    }

    context(_: PzlContext, cursor: PzlTokenCursor)
    override fun parse(
        typeSpec: TypeSpec?,
        contextSpec: ContextSpec?,
        modifiers: List<Modifier>
    ): EnumDeclaration {
        typeSpec?.check(TypeTarget.ENUM)
        modifiers.check(ModifierTarget.TOP_LEVEL_ENUM)
        return parseEnumDeclaration(typeSpec, contextSpec, modifiers)
    }
}