package puzzle.core.parser.matcher.declaration.toplevel

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.declaration.TraitDeclaration
import puzzle.core.parser.parser.binding.type.TypeTarget
import puzzle.core.parser.parser.binding.type.check
import puzzle.core.parser.parser.declaration.parseTraitDeclaration
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.modifier.check
import puzzle.core.symbol.Modifier

object TopLevelTraitDeclarationMatcher : TopLevelDeclarationMatcher<TraitDeclaration> {

    context(cursor: PzlTokenCursor)
    override fun match(): Boolean {
        return cursor.match(PzlTokenType.TRAIT)
    }

    context(_: PzlContext, cursor: PzlTokenCursor)
    override fun parse(
        typeSpec: TypeSpec?,
        contextSpec: ContextSpec?,
        modifiers: List<Modifier>
    ): TraitDeclaration {
        typeSpec?.check(TypeTarget.TRAIT)
        modifiers.check(ModifierTarget.TOP_LEVEL_TRAIT)
        return parseTraitDeclaration(typeSpec, contextSpec, modifiers)
    }
}