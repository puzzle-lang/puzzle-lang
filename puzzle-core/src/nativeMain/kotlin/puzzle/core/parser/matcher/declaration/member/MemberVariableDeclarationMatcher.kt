package puzzle.core.parser.matcher.declaration.member

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.declaration.VariableDeclaration
import puzzle.core.parser.parser.declaration.parseVariableDeclaration
import puzzle.core.symbol.Modifier

object MemberVariableDeclarationMatcher : MemberDeclarationMatcher<VariableDeclaration> {

    context(cursor: PzlTokenCursor)
    override fun match(): Boolean {
        return cursor.match(PzlTokenType.VAR) || cursor.match(PzlTokenType.VAL)
    }

    context(_: PzlContext, cursor: PzlTokenCursor)
    override fun parse(
        typeSpec: TypeSpec?,
        contextSpec: ContextSpec?,
        modifiers: List<Modifier>
    ): VariableDeclaration {
        return parseVariableDeclaration(typeSpec, contextSpec, modifiers)
    }
}